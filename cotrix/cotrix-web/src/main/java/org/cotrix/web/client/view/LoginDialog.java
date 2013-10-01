package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
		public void onCancel();
	}
	
	@UiField
	TextBox username;
	
	@UiField
	TextBox password;
	
	protected LoginDialogListener listener;

	public LoginDialog(LoginDialogListener listener) {
		this.listener = listener;
		setWidget(binder.createAndBindUi(this));
		username.getElement().setPropertyString("placeholder", "Username or Email");
		password.getElement().setPropertyString("placeholder", "Password");
	}
	
	@UiHandler("login")
	protected void onLogin(ClickEvent clickEvent)
	{
		listener.onLogin(username.getText(), password.getText());
	}
	
	@UiHandler("cancel")
	protected void onCancel(ClickEvent clickEvent)
	{
		listener.onCancel();
	}

}
