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
	PasswordTextBox password;

	@Inject
	protected void init(Binder binder) {
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}
	
	@UiHandler({"password"})
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
	
	@UiHandler("create")
	protected void onUpdate(ClickEvent clickEvent)
	{
		doUpdate();
	}
	
	protected void doUpdate() {
		boolean valid = validate();
		if (valid) {
			fireEvent(new PassworUpdatedEvent(password.getText()));
			hide();
		}
	}
	
	protected boolean validate()
	{
		boolean valid = true;
		if (!AccountValidator.validatePassword(password.getText())) {
			password.setStyleName(style.invalidValue(), true);
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
		        password.setFocus(true);
		    }
		});
	}
	

	public void clean() {
		password.setText("");
	}
	
	public HandlerRegistration addPasswordUpdateHandler(PasswordUpdatedHandler handler)
	{
		return addHandler(handler, PassworUpdatedEvent.getType());
	}
	
	public interface PasswordUpdatedHandler extends EventHandler {
		void onAddUser(PassworUpdatedEvent event);
	}

	public static class PassworUpdatedEvent extends GwtEvent<PasswordUpdatedHandler> {

		public static Type<PasswordUpdatedHandler> TYPE = new Type<PasswordUpdatedHandler>();
		
		protected String password;

		public PassworUpdatedEvent(String password) {
			this.password = password;
		}

		/**
		 * @return the password
		 */
		public String getPassword() {
			return password;
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
