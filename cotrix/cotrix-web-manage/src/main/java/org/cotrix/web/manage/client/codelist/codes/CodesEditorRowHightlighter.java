/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.client.codelist.codes.event.MarkerHighlightEvent;
import org.cotrix.web.manage.client.codelist.codes.marker.GradientClassGenerator;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;
import org.cotrix.web.manage.client.codelist.codes.marker.MarkerTypeUtil;
import org.cotrix.web.manage.client.di.CodelistBus;

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
	void bind(@CodelistBus EventBus bus, CodesEditorRowHightlighterEventBinder binder) {
		binder.bindEventHandlers(this, bus);
	}

	int seed = 0;
	
	@Override
	public String getStyleNames(UICode row, int rowIndex) {
		
		if (row == null) return null;
		
		//no highlight necessary
		if (highlightedMarkers.isEmpty()) return null;
		
		List<MarkerType> markers = markerUtil.resolve(row.getAttributes());
		
		//no marker to highlight
		if (markers.isEmpty()) return null;
		
		markers.retainAll(highlightedMarkers);
		
		if (markers.size() == 1) return markers.get(0).getHighlightStyleName();
		
		if (markers.size() > 1) return generateGradient(markers);
		
		return null;
	}
	
	private String generateGradient(List<MarkerType> markers) {
		Set<String> colors = new HashSet<String>();
		for (MarkerType marker:markers) colors.add(marker.getBackgroundColor());
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
