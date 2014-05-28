/**
 * 
 */
package org.cotrix.web.manage.client.codelist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodesToolbarImpl extends Composite implements CodesToolbar {
	
	@UiTemplate("CodesToolbar.ui.xml")
	interface CodesToolbarUiBinder extends UiBinder<Widget, CodesToolbarImpl> {}
	private static CodesToolbarUiBinder uiBinder = GWT.create(CodesToolbarUiBinder.class);
	
	@UiField PushButton allColumns;
	@UiField PushButton allNormals;
	
	@UiField PushButton lock;
	@UiField PushButton unlock;
	@UiField PushButton seal;
	
	@UiField InlineLabel state;
	@UiField Image stateLoader;
	
	protected ToolBarListener listener;
	
	public CodesToolbarImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("allColumns")
	protected void onAllColumnsClick(ClickEvent event) {
		listener.onAction(Action.ALL_COLUMN);
	}
	
	@UiHandler("allNormals")
	protected void onAllNormalsClick(ClickEvent event) {
		listener.onAction(Action.ALL_NORMAL);
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

	@Override
	public void setListener(ToolBarListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void setState(String msg)
	{
		state.setText(msg);
	}
	
	@Override
	public void showStateLoader(boolean visible)
	{
		stateLoader.setVisible(visible);
		state.setVisible(!visible);
	}

	@Override
	public void setEnabled(Action action, boolean enabled) {
		switch (action) {
			case ALL_COLUMN: allColumns.setEnabled(enabled); break;
			case ALL_NORMAL: allNormals.setEnabled(enabled); break;
			case FINALIZE: seal.setEnabled(enabled); break;
			case LOCK: lock.setEnabled(enabled); break;
			case UNLOCK: unlock.setEnabled(enabled); break;
		}
	}

}
