package org.cotrix.web.client.dialog;

import javax.inject.Inject;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.util.AccountValidator;

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
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class RegisterDialogImpl extends PopupPanel implements RegisterDialog {
	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiTemplate("RegisterDialog.ui.xml")
	interface Binder extends UiBinder<Widget, RegisterDialogImpl> {}
	
	@UiField
	TextBox username;
	
	@UiField
	PasswordTextBox password;
	
	@UiField
	TextBox email;
	
	@Inject
	CommonResources resources;
	
	private RegisterDialogListener listener;

	public RegisterDialogImpl() {
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}
	
	/**
	 * @param listener the listener to set
	 */
	public void setListener(RegisterDialogListener listener) {
		this.listener = listener;
	}
	
	/**
	 * @return the listener
	 */
	public RegisterDialogListener getListener() {
		return listener;
	}

	@UiHandler({"username","password","email"})
	protected void onKeyDown(KeyDownEvent event)
	{
		 if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	    	 doRegister();
	     }
		 if (event.getSource() instanceof UIObject) {
			 UIObject uiObject = (UIObject)event.getSource();
			 uiObject.setStyleName(resources.css().dialogTextboxInvalid(), false);
		 }
	}
	
	@UiHandler("create")
	protected void onRegister(ClickEvent clickEvent)
	{
		doRegister();
	}
	
	protected void doRegister() {
		boolean valid = validate();
		if (valid && listener!=null) listener.onRegister(username.getText(), password.getText(), email.getText());
	}
	
	protected boolean validate()
	{
		boolean valid = true;
		if (!AccountValidator.validateUsername(username.getText())) {
			username.setStyleName(resources.css().dialogTextboxInvalid(), true);
			valid = false;
		}
		
		if (!AccountValidator.validatePassword(password.getText())) {
			password.setStyleName(resources.css().dialogTextboxInvalid(), true);
			valid = false;
		}
		
		if (!AccountValidator.validateEMail(email.getText())) {
			email.setStyleName(resources.css().dialogTextboxInvalid(), true);
			valid = false;
		}
		
		return valid;
	}

	protected void cleanValidation() {
		username.setStyleName(resources.css().dialogTextboxInvalid(), false);
		password.setStyleName(resources.css().dialogTextboxInvalid(), false);
		email.setStyleName(resources.css().dialogTextboxInvalid(), false);
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
		email.setText("");
		cleanValidation();
	}

}
