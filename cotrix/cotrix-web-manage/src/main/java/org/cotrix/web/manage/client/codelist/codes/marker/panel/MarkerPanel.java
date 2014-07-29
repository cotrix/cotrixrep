/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.panel;

import java.util.List;

import org.cotrix.web.common.client.widgets.CustomDisclosurePanel;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerEvent;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;
import org.cotrix.web.manage.client.codelist.codes.marker.panel.MarkerPanelHeader.HeaderListener;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyle;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerPanel extends Composite {
	
	private static String INACTIVE_BACKGROUND_COLOR = "#FBFBFB";
	private static String INACTIVE_LABEL_COLOR = "#d6d6d9";
	
	public interface MarkerPanelListener {
		public void onMarkerActived(String description);
		public void onMarkerUnactived();

		public void onSwitch(boolean isDown);
		
		public void onUpdate(String description);
	}

	private boolean editable;
	private boolean active;

	private MarkerPanelHeader header;
	private MarkerDetailsPanel detailsPanel;
	private MarkerPanelListener listener;
	private MarkerType markerType;
	private MarkerStyle markerStyle;
	
	private CustomDisclosurePanel disclosurePanel;

	public MarkerPanel(final MarkerType markerType, MarkerStyle markerStyle) {
		
		this.markerType = markerType;
		this.markerStyle = markerStyle;
		
		header = new MarkerPanelHeader();
		header.setHeaderLabel(markerType.getName());
		
		disclosurePanel = new CustomDisclosurePanel(header);
		disclosurePanel.setWidth("100%");
		disclosurePanel.setAnimationEnabled(true);

		detailsPanel = new MarkerDetailsPanel();
		disclosurePanel.add(detailsPanel);
		
		initWidget(disclosurePanel);

		detailsPanel.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				disclosurePanel.setOpen(false);
				fireUpdated(event.getValue());
			}
		});

		header.setListener(new HeaderListener() {

			@Override
			public void onSwitchChange(boolean isDown) {
				Log.trace("onSwitchChange "+isDown);
				onSwitch(isDown);
			}

			@Override
			public void onHeaderClicked() {
				onMarkerClicked();		
			}
		});

		detailsPanel.setReadOnly(true);
		setUnactive();
	}
	
	public void setActive(String description, String value) {
		setActive(true, description, value);
	}
	
	public void setUnactive() {
		setActive(false, "", "");
		openDetails(false);
	}
	
	private void setActive(boolean active, String description, String value) {
		Log.trace("setActive active: "+active+" description: "+description+" value: "+value);
		this.active = active;
		updateColors();
		
		detailsPanel.setDescription(description);
		
		List<MarkerEvent> events = markerType.getEventExtractor().extract(value);
		detailsPanel.setEvents(events);
		
		header.setActivationCheck(active);
		header.setClickEnabled(active);
		updateDescriptionEditability();
	}
	
	private void updateColors() {
		header.setBackgroundColor(active?markerStyle.getBackgroundColor():INACTIVE_BACKGROUND_COLOR);
		header.setLabelColor(active?markerStyle.getTextColor():INACTIVE_LABEL_COLOR);
	}
	

	private void onMarkerClicked() {
		if (!markerType.isReadOnly()) {
			if (active) fireUnactived();
			else fireActived();
		}
	}
	
	private void fireActived() {
		setActive("", "");
		if (listener!=null) listener.onMarkerActived(detailsPanel.getDescription());
	}
	
	private void fireUnactived() {
		setUnactive();
		if (listener!=null) listener.onMarkerUnactived();
	}
	
	private void fireUpdated(String description) {
		if (listener!=null) listener.onUpdate(description);
	}
	
	private void onSwitch(boolean isDown) {
		if (listener!=null) listener.onSwitch(isDown);
	}
	
	private void updateDescriptionEditability() {
		boolean descriptionEditable = !markerType.isDescriptionReadOnly() && editable && active;
		detailsPanel.setReadOnly(!descriptionEditable);
		boolean activable = !markerType.isReadOnly() && editable;
		header.setActivationCheckEnabled(activable);
	}
	
	private void openDetails(boolean open) {
		disclosurePanel.setOpen(open);
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		updateDescriptionEditability();
	}

	public void setListener(MarkerPanelListener listener) {
		this.listener = listener;
	}

	public void setSwitchDown(boolean down) {
		header.setSwitchDown(down);
	}
}
