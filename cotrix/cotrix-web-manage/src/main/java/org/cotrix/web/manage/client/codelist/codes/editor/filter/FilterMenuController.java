/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor.filter;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.cotrix.web.common.client.widgets.DatePickerPopup;
import org.cotrix.web.manage.client.codelist.codes.editor.filter.FilterMenu.Listener;
import org.cotrix.web.manage.client.codelist.codes.editor.filter.FilterMenu.MenuButton;
import org.cotrix.web.manage.client.codelist.codes.event.FilterOptionUpdatedEvent;
import org.cotrix.web.manage.client.codelist.codes.event.MarkerHighlightEvent;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.shared.filter.FilterOption;
import org.cotrix.web.manage.shared.filter.MarkerFilterOption;
import org.cotrix.web.manage.shared.filter.SessionStartedOption;
import org.cotrix.web.manage.shared.filter.SinceCreationOption;
import org.cotrix.web.manage.shared.filter.SinceDateOption;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FilterMenuController {
	
	interface FilterMenuControllerEventBinder extends EventBinder<FilterMenuController> {}
	
	private Set<String> highlightedMarkers = new HashSet<String>();
	
	@Inject
	private FilterMenu filterMenu;
	
	@Inject @CodelistBus 
	private EventBus bus;
	
	private ToggleButton target;
	
	private DatePickerPopup datePickerPopup;
	
	@Inject
	void init() {
		filterMenu.setListener(new Listener() {
			
			@Override
			public void onHide() {
				target.setDown(false);
			}
			
			@Override
			public void onButtonClicked(MenuButton button) {
				onMenuButtonClicked(button);
			}
		});
		
		datePickerPopup = new DatePickerPopup();
		
		datePickerPopup.addValueChangeHandler(new ValueChangeHandler<Date>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				fireOption(new SinceDateOption(event.getValue()));
			}
		});
		
		datePickerPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				target.setDown(false);
				filterMenu.hide();
			}
		});
		
		filterMenu.setHighlightItemEnabled(!highlightedMarkers.isEmpty());
	}
	
	@Inject
	void bind(FilterMenuControllerEventBinder binder) {
		binder.bindEventHandlers(this, bus);
	}
	
	public void bind(final ToggleButton target) {
		this.target = target;
		
		target.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (target.isDown()) filterMenu.show(target);
				else filterMenu.hide();
			}
		});
	}
	
	private void onMenuButtonClicked(MenuButton button) {
		Log.trace("onMenuButtonClicked button: "+button);
		switch (button) {
			case ALL: bus.fireEvent(new FilterOptionUpdatedEvent(Collections.<FilterOption>emptyList())); break;
			case HIGHLIGHTED: fireOption(new MarkerFilterOption(highlightedMarkers)); break;
			case RECENT_CHANGES: fireOption(new SessionStartedOption()); break;
			case SINCE_CREATION: fireOption(new SinceCreationOption()); break;
			case SINCE: showSinceMenu(); break;
		}
		
		if (button!=MenuButton.SINCE) target.setDown(false);
	}
	
	private void fireOption(FilterOption option) {
		bus.fireEvent(new FilterOptionUpdatedEvent(Collections.<FilterOption>singletonList(option)));
	}
	
	private void showSinceMenu() {
		datePickerPopup.showRelativeTo(filterMenu.getSinceItem());
	}
	
	@EventHandler
	void onMarkerHighlight(MarkerHighlightEvent event) {
		Log.trace("onMarkerHighlight "+event);
		switch (event.getAction()) {
			case ADD: highlightedMarkers.add(event.getMarkerType().getDefinitionName()); break;
			case REMOVE: highlightedMarkers.remove(event.getMarkerType().getDefinitionName()); break;
		}
		
		filterMenu.setHighlightItemEnabled(!highlightedMarkers.isEmpty());
		
		if (highlightedMarkers.isEmpty() && filterMenu.getSelectedButton() == MenuButton.HIGHLIGHTED) {
			filterMenu.setSelectedShowAll();
			onMenuButtonClicked(MenuButton.ALL);
		}
	}

}
