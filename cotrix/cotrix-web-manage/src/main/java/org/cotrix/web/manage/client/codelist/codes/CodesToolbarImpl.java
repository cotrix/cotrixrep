/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes;

import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;
import org.cotrix.web.manage.client.codelist.codes.marker.menu.MarkerMenu;
import org.cotrix.web.manage.client.codelist.codes.marker.menu.MarkerMenu.Listener;

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
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

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
	@UiField ToggleButton markersMenuButton;
	
	@UiField InlineLabel state;
	@UiField Image stateLoader;
	
	@UiField PushButton metadataButton;
	
	protected ToolBarListener listener;
	
	@Inject
	private MarkerMenu markerMenu;
	
	@Inject
	public void init() {
		initWidget(uiBinder.createAndBindUi(this));
		markerMenu.setListener(new Listener() {
			
			@Override
			public void onHide() {
				markersMenuButton.setDown(false);
			}
			
			@Override
			public void onButtonClicked(MarkerType marker, boolean selected) {
				listener.onMarkerMenu(marker, selected);
			}
		});
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
	
	@UiHandler("markersMenuButton")
	protected void onMarkersMenuButtonClick(ClickEvent event) {
		markerMenu.show(markersMenuButton);
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

	@SuppressWarnings("incomplete-switch")
	@Override
	public void setEnabled(Action action, boolean enabled) {
		switch (action) {
			case ALL_COLUMN: allColumns.setEnabled(enabled); break;
			case ALL_NORMAL: allNormals.setEnabled(enabled); break;
		}
	}

}
