/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.editor;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.client.codelist.codes.editor.filter.RowFilter;
import org.cotrix.web.manage.client.codelist.codes.event.MarkerHighlightEvent;
import org.cotrix.web.manage.client.codelist.codes.marker.GradientClassGenerator;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerTypeUtil;
import org.cotrix.web.manage.client.codelist.codes.marker.style.DefaultMarkerStyleProvider;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodesEditorRowHightlighter implements RowStyles<UICode> {
	
	interface CodesEditorRowHightlighterEventBinder extends EventBinder<CodesEditorRowHightlighter> {}
	
	private EnumSet<MarkerType> highlightedMarkers = EnumSet.noneOf(MarkerType.class);
	
	@Inject
	private MarkerTypeUtil markerUtil;
	
	@Inject
	private GradientClassGenerator gradientClassGenerator;
	
	@Inject
	private DefaultMarkerStyleProvider styleProvider;
	
	@Inject
	private RowFilter rowFilter;
	
	@Inject
	void bind(@CodelistBus EventBus bus, CodesEditorRowHightlighterEventBinder binder) {
		binder.bindEventHandlers(this, bus);
	}

	int seed = 0;
	
	@Override
	public String getStyleNames(UICode row, int rowIndex) {
		
		if (row == null) return null;
		
		String markerClass = getMarkerHighlightClass(row);;
		String filterClass = getRowFilterClass(row);
		
		return markerClass == null?filterClass:filterClass==null?markerClass:markerClass+" "+filterClass;

	}
	
	private String getRowFilterClass(UICode row) {
		return rowFilter.isActive()&&!rowFilter.accept(row)?CotrixManagerResources.INSTANCE.css().filteredRow():null;
	}
	
	private String getMarkerHighlightClass(UICode row) {
		//no highlight necessary
		if (highlightedMarkers.isEmpty()) return null;
		
		List<MarkerType> markers = markerUtil.resolve(row.getAttributes());
		
		//no marker to highlight
		if (markers.isEmpty()) return null;
		
		markers.retainAll(highlightedMarkers);
		
		if (markers.size() == 1) return styleProvider.getStyle(markers.get(0)).getHighlightStyleName();
		
		if (markers.size() > 1) return generateGradient(markers);
		
		return null;
	}
	
	private String generateGradient(List<MarkerType> markers) {
		Set<String> colors = new LinkedHashSet<String>();
		Collections.sort(markers);
		for (MarkerType marker:markers) colors.add(styleProvider.getStyle(marker).getBackgroundColor());
		return gradientClassGenerator.getClassName(colors);
	}
	
	@EventHandler
	void onMarkerHighlight(MarkerHighlightEvent event) {
		Log.trace("onMarkerHighlight "+event);
		switch (event.getAction()) {
			case ADD: highlightedMarkers.add(event.getMarkerType()); break;
			case REMOVE: highlightedMarkers.remove(event.getMarkerType()); break;
		}
	}

}
