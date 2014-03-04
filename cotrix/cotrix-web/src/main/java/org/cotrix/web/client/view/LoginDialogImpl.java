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
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LoginDialogImpl extends PopupPanel implements LoginDialog {
	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiTemplate("LoginDialog.ui.xml")
	interface Binder extends UiBinder<Widget, LoginDialogImpl> {}
	
	@UiField
	TextBox username;
	
	@UiField
	PasswordTextBox password;
	
	private LoginDialogListener listener;

	public LoginDialogImpl() {
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}
	
	/**
	 * @param listener the listener to set
	 */
	public void setListener(LoginDialogListener listener) {
		this.listener = listener;
	}

	/**
	 * @return the listener
	 */
	public LoginDialogListener getListener() {
		return listener;
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
		if (listener!=null) listener.onRegister();
	}
	
	protected void doLogin() {
		if (listener!=null) listener.onLogin(username.getText(), password.getText());
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void showCentered() {
		
		super.center();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

		    @Override
		    public void execute() {
		        username.setFocus(true);
		    }
		});
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void clean() {
		username.setText("");
		password.setText("");
	}
	
}
