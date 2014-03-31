package org.cotrix.web.users.client.profile;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.util.AccountValidator;

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
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PasswordUpdateDialogImpl extends PopupPanel implements PasswordUpdateDialog {
	
	@UiTemplate("PasswordUpdateDialog.ui.xml")
	interface Binder extends UiBinder<Widget, PasswordUpdateDialogImpl> {}
	
	@UiField
	PasswordTextBox oldpassword;
	
	@UiField
	PasswordTextBox newpassword;
	
	private PasswordUpdateListener listener;
	
	@Inject
	CommonResources resources;

	@Inject
	protected void init(Binder binder) {
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}
	
	/**
	 * @param listener the listener to set
	 */
	public void setListener(PasswordUpdateListener listener) {
		this.listener = listener;
	}

	@UiHandler({"oldpassword","newpassword"})
	protected void onKeyDown(KeyDownEvent event)
	{
		 if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	    	 doUpdate();
	     }
		 if (event.getSource() instanceof UIObject) {
			 UIObject uiObject = (UIObject)event.getSource();
			 uiObject.setStyleName(resources.css().dialogTextboxInvalid(), false);
		 }
	}
	
	@UiHandler("update")
	protected void onUpdate(ClickEvent clickEvent)
	{
		doUpdate();
	}
	
	protected void doUpdate() {
		boolean valid = validate();
		if (valid && listener!=null) listener.onPasswordUpdate(oldpassword.getText(), newpassword.getText());
		if (valid)	hide();
		
	}
	
	protected boolean validate()
	{
		boolean valid = true;
		if (!AccountValidator.validatePassword(oldpassword.getText())) {
			oldpassword.setStyleName(resources.css().dialogTextboxInvalid(), true);
			valid = false;
		}
		
		if (!AccountValidator.validatePassword(newpassword.getText())) {
			newpassword.setStyleName(resources.css().dialogTextboxInvalid(), true);
			valid = false;
		}
		
		return valid;
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
		        oldpassword.setFocus(true);
		    }
		});
	}
	

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void clean() {
		oldpassword.setText("");
		newpassword.setText("");
	}
}
