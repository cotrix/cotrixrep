package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LoginDialog extends PopupPanel {
	
	private static final Binder binder = GWT.create(Binder.class);
	interface Binder extends UiBinder<Widget, LoginDialog> {}
	
	public interface LoginDialogListener {
		public void onLogin(String username, String password);
		public void onRegister();
		public void onCancel();
	}
	
	@UiField
	TextBox username;
	
	@UiField
	PasswordTextBox password;
	
	protected LoginDialogListener listener;

	public LoginDialog(LoginDialogListener listener) {
		this.listener = listener;
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}
	
	@UiHandler({"username","password"})
	protected void onKeyDown(KeyDownEvent event)
	{
		 if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			 doLogin();
	     }
	}
	
	@UiHandler("login")
	protected void onLogin(ClickEvent clickEvent)
	{
		doLogin();
	}
	
	@UiHandler("register")
	protected void onRegister(ClickEvent clickEvent) {
		listener.onRegister();
	}
	
	protected void doLogin() {
		listener.onLogin(username.getText(), password.getText());
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void show() {
		super.show();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

		    @Override
		    public void execute() {
		        username.setFocus(true);
		    }
		});
	}

	public void clean() {
		username.setText("");
		password.setText("");
	}
	
}
