package org.cotrix.web.users.client.profile;

import org.cotrix.web.common.client.util.AccountValidator;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PasswordUpdateDialog extends PopupPanel {
	
	interface Binder extends UiBinder<Widget, PasswordUpdateDialog> {}
	
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
	PasswordTextBox oldpassword;
	
	@UiField
	PasswordTextBox newpassword;

	@Inject
	protected void init(Binder binder) {
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}
	
	@UiHandler({"oldpassword","newpassword"})
	protected void onKeyDown(KeyDownEvent event)
	{
		 if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	    	 doUpdate();
	     }
		 if (event.getSource() instanceof UIObject) {
			 UIObject uiObject = (UIObject)event.getSource();
			 uiObject.setStyleName(style.invalidValue(), false);
		 }
	}
	
	@UiHandler("update")
	protected void onUpdate(ClickEvent clickEvent)
	{
		doUpdate();
	}
	
	protected void doUpdate() {
		boolean valid = validate();
		if (valid) {
			fireEvent(new PasswordUpdatedEvent(oldpassword.getText(), newpassword.getText()));
			hide();
		}
	}
	
	protected boolean validate()
	{
		boolean valid = true;
		if (!AccountValidator.validatePassword(newpassword.getText())) {
			newpassword.setStyleName(style.invalidValue(), true);
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
		        oldpassword.setFocus(true);
		    }
		});
	}
	

	public void clean() {
		oldpassword.setText("");
		newpassword.setText("");
	}
	
	public HandlerRegistration addPasswordUpdateHandler(PasswordUpdatedHandler handler)
	{
		return addHandler(handler, PasswordUpdatedEvent.getType());
	}
	
	public interface PasswordUpdatedHandler extends EventHandler {
		void onAddUser(PasswordUpdatedEvent event);
	}

	public static class PasswordUpdatedEvent extends GwtEvent<PasswordUpdatedHandler> {

		public static Type<PasswordUpdatedHandler> TYPE = new Type<PasswordUpdatedHandler>();
		
		protected String oldPassword;
		protected String newPassword;
		

		public PasswordUpdatedEvent(String oldPassword, String newPassword) {
			this.oldPassword = oldPassword;
			this.newPassword = newPassword;
		}

		/**
		 * @return the oldPassword
		 */
		public String getOldPassword() {
			return oldPassword;
		}

		/**
		 * @return the newPassword
		 */
		public String getNewPassword() {
			return newPassword;
		}

		@Override
		protected void dispatch(PasswordUpdatedHandler handler) {
			handler.onAddUser(this);
		}

		@Override
		public Type<PasswordUpdatedHandler> getAssociatedType() {
			return TYPE;
		}

		public static Type<PasswordUpdatedHandler> getType() {
			return TYPE;
		}
	}

	
}
