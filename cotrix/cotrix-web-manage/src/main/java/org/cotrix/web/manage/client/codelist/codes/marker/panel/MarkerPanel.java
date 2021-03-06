/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.panel;

import java.util.List;

import org.cotrix.web.common.client.widgets.CustomDisclosurePanel;
import org.cotrix.web.common.client.widgets.button.ButtonResources;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;
import org.cotrix.web.manage.client.codelist.codes.marker.event.MarkerEvent;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyle;
import org.cotrix.web.manage.client.codelist.common.header.HeaderPanel;

import static org.cotrix.web.common.client.widgets.button.ButtonResourceBuilder.*;
import static org.cotrix.web.manage.client.codelist.common.Icons.icons;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerPanel extends Composite {
	
	private static ButtonResources SWITCH = create().upFace(icons.markerUnchecked()).hover(icons.markerUnchecked()).downFace(icons.markerChecked()).title("Activate").build();
	
	public interface MarkerPanelListener {
		public void onMarkerActived(String description);
		public void onMarkerUnactived();
		
		public void onUpdate(String description);
	}

	private boolean editable;
	private boolean active;

	private HeaderPanel header;
	private MarkerDetailsPanel detailsPanel;
	private MarkerPanelListener listener;
	private MarkerType markerType;
	private MarkerStyle markerStyle;
	
	private CustomDisclosurePanel disclosurePanel;

	public MarkerPanel(final MarkerType markerType, MarkerStyle markerStyle) {
		
		this.markerType = markerType;
		this.markerStyle = markerStyle;
		
		header = new HeaderPanel();
		header.setIcon(icons.marker());
		header.setSwitchButton(SWITCH);
		header.setSwitchVisible(true);
		
		header.setTitle(markerType.getName());
		header.setSubtitle(markerType.getDescription());
		
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
		
		header.addSwitchButtonClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				onMarkerClicked();	
			}
		});
		
		header.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				disclosurePanel.toggle();
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
		
		header.setSwitchDown(active);
		updateDescriptionEditability();
	}
	
	private void updateColors() {
		if (active) header.setBackgroundColor(markerStyle.getBackgroundColor());
		else header.resetBackgroundColor();
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
	
	private void updateDescriptionEditability() {
		boolean descriptionEditable = !markerType.isDescriptionReadOnly() && editable && active;
		detailsPanel.setReadOnly(!descriptionEditable);
		boolean activable = !markerType.isReadOnly() && editable;
		header.setSwitchVisible(activable);
	}
	
	public void openDetails(boolean open) {
		disclosurePanel.setOpen(open);
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		updateDescriptionEditability();
	}

	public void setListener(MarkerPanelListener listener) {
		this.listener = listener;
	}
}
