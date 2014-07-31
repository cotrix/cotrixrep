/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.style;

import java.util.Map;

import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class DefaultMarkerStyleProvider implements MarkerStyleProvider {
	
	interface DefaultBinder extends MarkerStyleResourceBinder<MarkersResource>{};

	private Map<MarkerType, MarkerStyle> styles;
	
	@Inject
	void init(DefaultBinder binder) {
		styles = binder.bind(MarkersResource.INSTANCE);
	}
	
	@Override
	public MarkerStyle getStyle(MarkerType type) {
		return styles.get(type);
	}

}
