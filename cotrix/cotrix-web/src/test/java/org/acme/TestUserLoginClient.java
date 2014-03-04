/**
 * 
 */
package org.acme;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.client.event.UserLoginEvent;
import org.cotrix.web.client.presenter.UserBarPresenter;
import org.cotrix.web.client.presenter.UserController;
import org.cotrix.web.client.view.LoginDialog;
import org.cotrix.web.client.view.LoginDialog.LoginDialogListener;
import org.cotrix.web.client.view.RegisterDialog;
import org.cotrix.web.client.view.UserBarView;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.UserLoggedEvent;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureBus;
import org.cotrix.web.common.shared.UIUser;
import org.cotrix.web.common.shared.exception.IllegalActionException;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.cotrix.web.shared.UsernamePasswordToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.testing.CountingEventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TestUserLoginClient {

	@Mock
	private UserBarView userBarView;

	private CountingEventBus bus = new CountingEventBus();

	private LoginDialog loginDialog;

	@Mock
	private RegisterDialog registerDialog;

	private UserBarPresenter presenter;

	@Mock
	private MainServiceAsync service;

	private class TestModule extends AbstractModule {

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
			return service;
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

		loginDialog = mock(FakeLoginDialog.class);
		doCallRealMethod().when(loginDialog).setListener(any(LoginDialogListener.class));
		doCallRealMethod().when(loginDialog).getListener();

		Module testModule = new TestModule();
		Injector injector = Guice.createInjector(testModule);
		presenter = injector.getInstance(UserBarPresenter.class);
		injector.getInstance(UserController.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testLogin() throws IllegalActionException, ServiceException {

		UIUser expectedUser = new UIUser("fakeId", "fakeUsername", "fakeName");
		UsernamePasswordToken expectedToken = new UsernamePasswordToken("fakeUsername", "fakePassword");
		doAnswer(new SuccessAnswer<>(expectedUser)).when(service).login(any(UsernamePasswordToken.class), Mockito.anyList(), any(AsyncCallback.class));

		presenter.onLoginClick();
		verify(loginDialog).showCentered();

		LoginDialogListener listener = loginDialog.getListener();
		assertNotNull(listener);

		listener.onLogin(expectedToken.getUsername(), expectedToken.getPassword());
		assertEquals(bus.getFiredCount(UserLoginEvent.TYPE),1);

		verify(service).login(eq(expectedToken), Mockito.anyList(), any(AsyncCallback.class));

		assertEquals(bus.getFiredCount(UserLoggedEvent.TYPE),1);

		verify(userBarView).setUserEnabled(true);
		verify(userBarView).setUserLoading(false);
		verify(userBarView).setUsername(expectedToken.getUsername());
	}

	private static class SuccessAnswer<T> implements Answer<T> {

		private final T result;

		public SuccessAnswer(T result) {
			this.result = result;
		}

		@SuppressWarnings("unchecked")
		public T answer(InvocationOnMock invocation) {
			Object[] arguments = invocation.getArguments();
			AsyncCallback<Object> callback = (AsyncCallback<Object>) arguments[arguments.length - 1];
			callback.onSuccess(result);
			return null;
		}

	}

	private abstract class FakeLoginDialog implements LoginDialog {

		LoginDialogListener listener;

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setListener(LoginDialogListener listener) {
			this.listener = listener;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public LoginDialogListener getListener() {
			return listener;
		}		
	}
}
