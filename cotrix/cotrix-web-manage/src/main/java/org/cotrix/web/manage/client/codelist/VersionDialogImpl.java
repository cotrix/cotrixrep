package org.cotrix.web.manage.client.codelist;

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
import com.google.gwt.user.client.ui.Label;
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
public class VersionDialogImpl extends PopupPanel implements VersionDialog {

	private static final Binder binder = GWT.create(Binder.class);

	@UiTemplate("VersionDialog.ui.xml")
	interface Binder extends UiBinder<Widget, VersionDialogImpl> {}

	@UiField Label name;

	@UiField TextBox oldVersion;

	@UiField TextBox newVersion;

	@Inject
	CommonResources resources;

	private String id;
	private VersionDialogListener listener;

	public VersionDialogImpl() {
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}

	@UiHandler({"newVersion"})
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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setListener(VersionDialogListener listener) {
		this.listener = listener;
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
				newVersion.setFocus(true);
			}
		});
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setOldVersion(String id, String name, String oldVersion)
	{
		this.id = id;
		this.name.setText(name);
		this.oldVersion.setText(oldVersion);
		this.newVersion.setText(oldVersion);
		cleanValidation();
	}

	@UiHandler("create")
	protected void onCreate(ClickEvent clickEvent)
	{
		fireCreate();
	}

	protected boolean validate() {
		boolean valid = true;

		if (!CodelistInfoValidator.validateVersion(newVersion.getText())) {
			newVersion.setStyleName(resources.css().dialogTextboxInvalid(), true);
			valid = false;
		}

		return valid;
	}

	protected void cleanValidation() {
		newVersion.setStyleName(resources.css().dialogTextboxInvalid(), false);
	}

	protected void fireCreate() {
		boolean valid = validate();
		if (valid && listener!=null) listener.onCreate(id, newVersion.getValue());
		if (valid) hide();
	}

}
