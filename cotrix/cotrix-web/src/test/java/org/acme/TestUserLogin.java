/**
 * 
 */
package org.acme;

import static org.cotrix.domain.dsl.Roles.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.mockito.Mockito.*;

import java.util.List;

import javax.inject.Inject;

import org.cotrix.domain.user.User;
import org.cotrix.security.SignupService;
import org.cotrix.test.ApplicationTest;
import org.cotrix.web.client.MainService;
import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.client.presenter.UserBarPresenter;
import org.cotrix.web.client.presenter.UserController;
import org.cotrix.web.client.view.LoginDialog;
import org.cotrix.web.client.view.RegisterDialog;
import org.cotrix.web.client.view.UserBarView;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureBus;
import org.cotrix.web.common.client.widgets.AlertDialog;
import org.cotrix.web.common.shared.UIUser;
import org.cotrix.web.common.shared.exception.IllegalActionException;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.server.MainServiceImpl;
import org.cotrix.web.shared.LoginToken;
import org.cotrix.web.shared.UINews;
import org.cotrix.web.shared.UIStatistics;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestUserLogin extends ApplicationTest {
	
	private static final User testUser = user().name("testUser").email("testUser@fao.org").fullName("Test User").is(ROOT).build();
	private static final String testUserPassword = "testPassword";
	
	@Mock
	private UserBarView userBarView;

	@Mock
	private LoginDialog loginDialog;

	@Mock
	private RegisterDialog registerDialog;
	
	@Mock
	private AlertDialog alertDialog;

	private UserBarPresenter presenter;

	@Inject
	private MainServiceImpl service;
	
	@Inject
	private SignupService signupService;

	private MainServiceAsync serviceAsync;

	private class TestModule extends AbstractModule {
		
		private EventBus bus = new SimpleEventBus();

		@Override
		protected void configure() {
			requestStaticInjection(FeatureBinder.class);
		}

		@Provides
		UserBarView getUserBarView() {
			return userBarView;
		}

		@Provides @CotrixBus
		EventBus getEventBus() {
			return bus;
		}

		@Provides
		LoginDialog getLoginDialog() {
			return loginDialog;
		}

		@Provides
		RegisterDialog getRegisterDialog() {
			return registerDialog;
		}

		@Provides
		MainServiceAsync getMainService() {
			return serviceAsync;
		}
		
		@Provides
		AlertDialog getAlertDialog() {
			return alertDialog;
		}

		@Provides
		@Singleton
		@FeatureBus
		protected EventBus getFeatureBus()
		{
			return new SimpleEventBus();
		}
	}

	@Before
	public void setup() {

		MockitoAnnotations.initMocks(this);

		serviceAsync = new MainServiceBridge(service);

		Module testModule = new TestModule();
		Injector injector = Guice.createInjector(testModule);
		presenter = injector.getInstance(UserBarPresenter.class);
		injector.getInstance(UserController.class);
		
		signupService.signup(testUser, testUserPassword);
	}
	
	@Test
	public void testLogin() throws IllegalActionException, ServiceException {
		
		//User clicks login
		presenter.onLoginClick();
		
		//Login dialog appears
		verify(loginDialog).showCentered();

		//User fills login form and he submits it
		presenter.onLogin(testUser.name(), testUserPassword);
		
		//User is logged
		verify(userBarView).setUsername(testUser.name());
	}

	private class MainServiceBridge implements MainServiceAsync {

		private MainService service;

		/**
		 * @param service
		 */
		private MainServiceBridge(MainService service) {
			this.service = service;
		}

		@Override
		public void getCurrentUser(AsyncCallback<UIUser> callback) {
			// TODO Auto-generated method stub

		}

		@Override
		public void login(LoginToken token, List<String> openCodelists,
				AsyncCallback<UIUser> callback) {
			try {
				callback.onSuccess(service.login(token, openCodelists));
			} catch (Exception e) {
				callback.onFailure(e);
			}			
		}

		@Override
		public void logout(List<String> openCodelists,
				AsyncCallback<UIUser> callback) {
			// TODO Auto-generated method stub

		}

		@Override
		public void registerUser(String username, String password,
				String email, List<String> openCodelists,
				AsyncCallback<UIUser> callback) {
			// TODO Auto-generated method stub

		}

		@Override
		public void getStatistics(AsyncCallback<UIStatistics> callback) {
			// TODO Auto-generated method stub

		}

		@Override
		public void getNews(AsyncCallback<List<UINews>> callback) {
			// TODO Auto-generated method stub

		}

	}

}
