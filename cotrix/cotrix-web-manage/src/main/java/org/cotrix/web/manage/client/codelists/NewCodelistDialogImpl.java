package org.cotrix.web.manage.client.codelists;

import javax.inject.Inject;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.util.CodelistInfoValidator;

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
public class NewCodelistDialogImpl extends PopupPanel implements NewCodelistDialog {
	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiTemplate("NewCodelistDialog.ui.xml")
	interface Binder extends UiBinder<Widget, NewCodelistDialogImpl> {}
	
	@UiField TextBox name;
	
	@UiField TextBox version;
	
	@Inject
	CommonResources resources;

	private NewCodelistDialogListener listener;

	public NewCodelistDialogImpl() {
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setListener(NewCodelistDialogListener listener) {
		this.listener = listener;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void showCentered() {
		super.center();
		clean();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

		    @Override
		    public void execute() {
		    	name.setFocus(true);
		    }
		});
	}
	
	@UiHandler({"name","version"})
	protected void onKeyDown(KeyDownEvent event)
	{
		 if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			 fireCreate();
	     }
		 if (event.getSource() instanceof UIObject) {
			 UIObject uiObject = (UIObject)event.getSource();
			 uiObject.setStyleName(resources.css().dialogTextboxInvalid(), false);
		 }
	}
	
	protected boolean validate()
	{
		boolean valid = true;
		if (!CodelistInfoValidator.validateName(name.getText())) {
			name.setStyleName(resources.css().dialogTextboxInvalid(), true);
			valid = false;
		}
		
		if (!CodelistInfoValidator.validateVersion(version.getText())) {
			version.setStyleName(resources.css().dialogTextboxInvalid(), true);
			valid = false;
		}
		
		return valid;
	}
	
	protected void cleanValidation() {
		name.setStyleName(resources.css().dialogTextboxInvalid(), false);
		version.setStyleName(resources.css().dialogTextboxInvalid(), false);
	}
	
	@UiHandler("create")
	protected void onCreate(ClickEvent clickEvent)
	{
		fireCreate();
	}
	
	protected void fireCreate() {
		boolean valid = validate();
		if (valid && listener!=null) listener.onCreate(name.getValue(), version.getValue());
		if (valid) hide();
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void clean() {
		name.setText("");
		version.setText("");
		cleanValidation();
	}

}
