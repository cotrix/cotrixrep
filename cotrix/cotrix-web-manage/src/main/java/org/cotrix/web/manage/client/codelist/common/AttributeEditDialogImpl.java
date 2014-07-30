package org.cotrix.web.manage.client.codelist.common;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.widgets.AdvancedTextBox;
import org.cotrix.web.common.client.widgets.LanguageListBox;
import org.cotrix.web.common.client.widgets.UIQNameBox;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIQName;

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
	
	@UiField UIQNameBox nameBox;
	@UiField UIQNameBox typeBox;
	@UiField AdvancedTextBox descriptionBox;
	@UiField LanguageListBox languageBox;
	@UiField AdvancedTextBox valueBox;
	
	private CommonResources resources = CommonResources.INSTANCE;
	
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

	@UiHandler({"nameBox","typeBox","descriptionBox","valueBox"})
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
		if (listener!=null) listener.onEdit(nameBox.getValue(), typeBox.getValue(), descriptionBox.getText(), languageBox.getValue(), valueBox.getText());
	}

	protected void cleanValidation() {
		nameBox.setStyleName(resources.css().dialogTextboxInvalid(), false);
		typeBox.setStyleName(resources.css().dialogTextboxInvalid(), false);
		descriptionBox.setStyleName(resources.css().dialogTextboxInvalid(), false);
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
		nameBox.setValue(new UIQName("", ""));
		typeBox.setValue(new UIQName("", ""));
		languageBox.setValue(Language.NONE);
		valueBox.setText("");
		cleanValidation();
	}

	@Override
	public void set(UIQName name, UIQName type, String description, Language language, String value) {
		nameBox.setValue(name);
		typeBox.setValue(type);
		descriptionBox.setText(description);
		languageBox.setValue(language);
		valueBox.setText(value);
		cleanValidation();
	}

}
