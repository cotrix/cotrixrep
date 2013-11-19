package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RegisterDialog extends PopupPanel {
	
	protected static final RegExp EMAIL_EXP = RegExp.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$");
	
	private static final Binder binder = GWT.create(Binder.class);
	interface Binder extends UiBinder<Widget, RegisterDialog> {}
	
	public interface RegisterDialogListener {
		public void onRegister(String username, String password, String email);
		public void onCancel();
	}
	
	protected interface Style extends CssResource {
		String invalidValue();
	}
	
	@UiField
	Style style;
	
	@UiField
	TextBox username;
	
	@UiField
	PasswordTextBox password;
	
	@UiField
	TextBox email;
	
	protected RegisterDialogListener listener;

	public RegisterDialog(RegisterDialogListener listener) {
		this.listener = listener;
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}
	
	@UiHandler({"username","password","email"})
	protected void onKeyDown(KeyDownEvent event)
	{
		 if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	    	 doRegister();
	     }
		 if (event.getSource() instanceof UIObject) {
			 UIObject uiObject = (UIObject)event.getSource();
			 uiObject.setStyleName(style.invalidValue(), false);
		 }
	}
	
	@UiHandler("create")
	protected void onRegister(ClickEvent clickEvent)
	{
		doRegister();
	}
	
	protected void doRegister() {
		boolean valid = validate();
		if (valid) listener.onRegister(username.getText(), password.getText(), email.getText());
	}
	
	protected boolean validate()
	{
		boolean valid = true;
		if (username.getText() == null || username.getText().isEmpty()) {
			username.setStyleName(style.invalidValue(), true);
			valid = false;
		}
		
		if (password.getText() == null || password.getText().isEmpty()) {
			password.setStyleName(style.invalidValue(), true);
			valid = false;
		}
		
		if (email.getText() == null || email.getText().isEmpty() || !EMAIL_EXP.test(email.getText())) {
			email.setStyleName(style.invalidValue(), true);
			valid = false;
		}
		
		return valid;
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
		email.setText("");
	}

	
}
