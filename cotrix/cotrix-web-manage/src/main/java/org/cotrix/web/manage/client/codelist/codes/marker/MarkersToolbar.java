/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;

import org.cotrix.web.manage.client.codelist.codes.event.MarkerHighlightEvent;
import org.cotrix.web.manage.client.codelist.codes.event.MarkerHighlightEvent.Action;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyle;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyleProvider;
import org.cotrix.web.manage.client.di.CodelistBus;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import static org.cotrix.web.manage.client.codelist.codes.marker.style.MarkersResource.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkersToolbar extends Composite {
	
	@Inject @CodelistBus
	private EventBus bus;
	
	private FlowPanel toolbar;	
	
	@Inject
	public MarkersToolbar(MarkerStyleProvider styleProvider) {
		toolbar = new FlowPanel();
		initButtons(styleProvider);
		initWidget(toolbar);
	}
	
	private void initButtons(MarkerStyleProvider styleProvider) {
		for (MarkerType marker:MarkerType.values()) addButton(marker, styleProvider.getStyle(marker));
	}
	
	private void addButton(final MarkerType marker, MarkerStyle markerStyle) {
		MarkerButton button = new MarkerButton(marker.getName(), markerStyle);
		button.addStyleName(style.toolbarButton());
		button.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				fireEvent(marker, event.getValue());
			}
		});
		toolbar.add(button);
	}
	
	private void fireEvent(MarkerType type, boolean buttonDown) {
		bus.fireEvent(new MarkerHighlightEvent(type, buttonDown?Action.ADD:Action.REMOVE));
	}

}
