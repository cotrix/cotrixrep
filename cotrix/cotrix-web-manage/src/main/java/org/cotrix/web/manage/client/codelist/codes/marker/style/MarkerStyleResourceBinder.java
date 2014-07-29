/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.style;

import java.util.Map;

import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MarkerStyleResourceBinder<T extends MarkerStyleResource> {
	
	public Map<MarkerType, MarkerStyle> bind(T resource);

}
