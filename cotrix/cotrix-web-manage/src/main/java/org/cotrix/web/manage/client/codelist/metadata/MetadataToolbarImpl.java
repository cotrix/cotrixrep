/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataToolbarImpl extends Composite implements MetadataToolbar {
	
	@UiTemplate("MetadataToolbar.ui.xml")
	interface MetadataToolbarUiBinder extends UiBinder<Widget, MetadataToolbarImpl> {}
	private static MetadataToolbarUiBinder uiBinder = GWT.create(MetadataToolbarUiBinder.class);
	
	@UiField PushButton lock;
	@UiField PushButton unlock;
	@UiField PushButton seal;
	
	@UiField PushButton codesButton;
	
	protected ToolBarListener listener;
	
	public MetadataToolbarImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	@UiHandler("lock")
	protected void onLockClick(ClickEvent event) {
		listener.onAction(Action.LOCK);
	}
	
	@UiHandler("unlock")
	protected void onUnlockClick(ClickEvent event) {
		listener.onAction(Action.UNLOCK);
	}
	
	@UiHandler("seal")
	protected void onSealClick(ClickEvent event) {
		listener.onAction(Action.FINALIZE);
	}
	
	@UiHandler("codesButton")
	protected void onCodesClick(ClickEvent event) {
		listener.onAction(Action.TO_CODES);
	}

	@Override
	public void setListener(ToolBarListener listener) {
		this.listener = listener;
	}

	@Override
	public void setEnabled(Action action, boolean enabled) {
		switch (action) {
			case FINALIZE: seal.setEnabled(enabled); break;
			case LOCK: lock.setEnabled(enabled); break;
			case UNLOCK: unlock.setEnabled(enabled); break;
			case TO_CODES: codesButton.setEnabled(enabled); break;
		}
	}

}
