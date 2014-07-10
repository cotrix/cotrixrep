/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes;

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
	
	@UiField InlineLabel state;
	@UiField Image stateLoader;
	
	@UiField PushButton metadataButton;
	
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
	
	@UiHandler("metadataButton")
	protected void onMetadataClick(ClickEvent event) {
		listener.onAction(Action.TO_METADATA);
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
		}
	}

}