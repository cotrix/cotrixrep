/**
 * 
 */
package org.acme;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.cotrix.web.client.event.UserLoginEvent;
import org.cotrix.web.client.event.UserLogoutEvent;
import org.cotrix.web.client.event.UserRegisterEvent;
import org.cotrix.web.client.presenter.UserBarPresenter;
import org.cotrix.web.client.view.LoginDialog;
import org.cotrix.web.client.view.LoginDialog.LoginDialogListener;
import org.cotrix.web.client.view.RegisterDialog;
import org.cotrix.web.client.view.RegisterDialog.RegisterDialogListener;
import org.cotrix.web.client.view.UserBarView;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.UserLoggedEvent;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureBus;
import org.cotrix.web.common.client.widgets.AlertDialog;
import org.cotrix.web.common.shared.UIUser;
import org.cotrix.web.common.shared.exception.IllegalActionException;
import org.cotrix.web.common.shared.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.binder.impl.GenericEventType;
import com.google.web.bindery.event.shared.testing.CountingEventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TestUserBar {

	@Mock
	private UserBarView userBarView;
	
	private CountingEventBus bus = new CountingEventBus();
	
	private LoginDialog loginDialog;
	
	private RegisterDialog registerDialog;
	
	private UserBarPresenter presenter;
	
	private class TestModule extends AbstractModule {

		@Override
		protected void configure() {
			requestStaticInjection(FeatureBinder.class);
			bind(UserBarView.class).toInstance(userBarView);
			bind(LoginDialog.class).toInstance(loginDialog);
			bind(RegisterDialog.class).toInstance(registerDialog);
			bind(AlertDialog.class).toInstance(mock(AlertDialog.class));
			bind(EventBus.class).annotatedWith(CotrixBus.class).toInstance(bus);
			bind(EventBus.class).annotatedWith(FeatureBus.class).toInstance(new SimpleEventBus());
			
		}
	}
	
	@Before
	public void setup() {
		
		loginDialog = mock(FakeLoginDialog.class);
		doCallRealMethod().when(loginDialog).setListener(any(LoginDialogListener.class));
		doCallRealMethod().when(loginDialog).getListener();
		
		registerDialog = mock(FakeRegisterDialog.class);
		doCallRealMethod().when(registerDialog).setListener(any(RegisterDialogListener.class));
		doCallRealMethod().when(registerDialog).getListener();
		
		Module testModule = new TestModule();
		Injector injector = Guice.createInjector(testModule);
		presenter = injector.getInstance(UserBarPresenter.class);
	}
	
	@Test
	public void testShowLoginDialog() throws IllegalActionException, ServiceException {
		presenter.onLoginClick();
		verify(loginDialog).showCentered();
	}
	
	@Test
	public void testShowRegisterDialog() throws IllegalActionException, ServiceException {
		presenter.onRegisterClick();
		verify(registerDialog).showCentered();
	}
	
	@Test
	public void testSendUserLoginEvent() throws IllegalActionException, ServiceException {
		LoginDialogListener listener = loginDialog.getListener();
		assertNotNull(listener);
		
		listener.onLogin("fakeUsername", "fakePassword");

		assertEquals(bus.getFiredCount(GenericEventType.getTypeOf(UserLoginEvent.class)),1);
	}
	
	@Test
	public void testSendUserRegisterEvent() throws IllegalActionException, ServiceException {
		RegisterDialogListener listener = registerDialog.getListener();
		assertNotNull(listener);
		
		listener.onRegister("fakeUsername", "fakePassword", "fakeEmail");

		assertEquals(bus.getFiredCount(GenericEventType.getTypeOf(UserRegisterEvent.class)),1);
	}
	
	@Test
	public void testSendUserLogoutEvent() throws IllegalActionException, ServiceException {
		presenter.onLogoutClick();

		assertEquals(bus.getFiredCount(GenericEventType.getTypeOf(UserLogoutEvent.class)),1);
	}
	
	@Test
	public void testUserLoggedEventReaction() throws IllegalActionException, ServiceException {
		bus.fireEvent(new UserLoggedEvent(new UIUser("fakeId", "fakeUsername", "fakeName")));

		verify(userBarView).setUserEnabled(true);
		verify(userBarView).setUserLoading(false);
		verify(userBarView).setUsername("fakeUsername");
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
	
	private abstract class FakeRegisterDialog implements RegisterDialog {
		
		RegisterDialogListener listener;

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setListener(RegisterDialogListener listener) {
			this.listener = listener;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public RegisterDialogListener getListener() {
			return listener;
		}		
	}

}
