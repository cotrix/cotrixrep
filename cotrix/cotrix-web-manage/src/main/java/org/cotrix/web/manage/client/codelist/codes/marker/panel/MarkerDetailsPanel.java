/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.panel;

import java.util.List;

import org.cotrix.web.common.client.widgets.AutoHeightTextArea;
import org.cotrix.web.manage.client.codelist.codes.marker.parse.MarkerEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerDetailsPanel extends Composite implements HasValueChangeHandlers<String>{

	private static MarkerDetailsPanelUiBinder uiBinder = GWT
			.create(MarkerDetailsPanelUiBinder.class);

	interface MarkerDetailsPanelUiBinder extends
			UiBinder<Widget, MarkerDetailsPanel> {
	}

	@UiField
	AutoHeightTextArea descriptionArea;
	
	@UiField
	VerticalPanel eventsContainer;

	public MarkerDetailsPanel() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public String getDescription() {
		return descriptionArea.getValue();
	}
	
	public void setDescription(String description) {
		descriptionArea.setValue(description);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return descriptionArea.addValueChangeHandler(handler);
	}

	public void setReadOnly(boolean readOnly) {
		descriptionArea.setEnabled(!readOnly);
	}

	public void focus() {
		descriptionArea.setFocus(true);		
	}
	
	public void setEvents(List<MarkerEvent> events) {
		eventsContainer.clear();
		for (MarkerEvent event:events) {
			MarkerEventPanel eventPanel = new MarkerEventPanel();
			eventPanel.setEvent(event);
			eventsContainer.add(eventPanel);
		}
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		descriptionArea.setVisible(visible);
	}
	
}
