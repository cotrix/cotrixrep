package org.cotrix.web.manage.client.codelist.codes.event;

import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerHighlightEvent extends GenericEvent {

	public enum Action {ADD, REMOVE};
	
	private MarkerType markerType;
	private Action action;

	public MarkerHighlightEvent(MarkerType markerType, Action action) {
		this.markerType = markerType;
		this.action = action;
	}
	
	public MarkerType getMarkerType() {
		return markerType;
	}
	
	public Action getAction() {
		return action;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MarkerHighlightEvent [markerType=");
		builder.append(markerType);
		builder.append(", action=");
		builder.append(action);
		builder.append("]");
		return builder.toString();
	}	
}
