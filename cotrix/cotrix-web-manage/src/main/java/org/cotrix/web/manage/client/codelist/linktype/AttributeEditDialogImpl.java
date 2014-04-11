package org.cotrix.web.manage.client.codelist.linktype;

import javax.inject.Inject;

import org.cotrix.web.common.client.resources.CommonResources;

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
public class AttributeEditDialogImpl extends PopupPanel implements AttributeEditDialog {
	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiTemplate("AttributeEditDialog.ui.xml")
	interface Binder extends UiBinder<Widget, AttributeEditDialogImpl> {}
	
	@UiField TextBox nameBox;
	@UiField TextBox typeBox;
	@UiField TextBox languageBox;
	@UiField TextBox valueBox;
	
	//FIXME @inject
	CommonResources resources = CommonResources.INSTANCE;
	
	private AttributeEditDialogListener listener;

	public AttributeEditDialogImpl() {
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}
	
	/**
	 * @param listener the listener to set
	 */
	public void setListener(AttributeEditDialogListener listener) {
		this.listener = listener;
	}

	@UiHandler({"nameBox","typeBox","languageBox","valueBox"})
	protected void onKeyDown(KeyDownEvent event)
	{
		 if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			 doEdit();
	     }
		 if (event.getSource() instanceof UIObject) {
			 UIObject uiObject = (UIObject)event.getSource();
			 uiObject.setStyleName(resources.css().dialogTextboxInvalid(), false);
		 }
	}
	
	@UiHandler("save")
	protected void onSave(ClickEvent clickEvent)
	{
		doEdit();
	}
	
	@UiHandler("cancel")
	protected void onCancel(ClickEvent clickEvent)
	{
		hide();
	}
	
	protected void doEdit() {
		boolean valid = validate();
		if (valid && listener!=null) listener.onEdit(nameBox.getText(), typeBox.getText(), languageBox.getText(), valueBox.getText());
	}
	
	protected boolean validate()
	{
		boolean valid = true;
		/*if (!AccountValidator.validateUsername(username.getText())) {
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
		}*/
		
		return valid;
	}

	protected void cleanValidation() {
		nameBox.setStyleName(resources.css().dialogTextboxInvalid(), false);
		typeBox.setStyleName(resources.css().dialogTextboxInvalid(), false);
		languageBox.setStyleName(resources.css().dialogTextboxInvalid(), false);
		valueBox.setStyleName(resources.css().dialogTextboxInvalid(), false);
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
		    	nameBox.setFocus(true);
		    }
		});
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void clean() {
		nameBox.setText("");
		typeBox.setText("");
		languageBox.setText("");
		valueBox.setText("");
		cleanValidation();
	}

	@Override
	public void set(String name, String type, String language, String value) {
		nameBox.setText(name);
		typeBox.setText(type);
		languageBox.setText(language);
		valueBox.setText(value);
		cleanValidation();
	}

}
