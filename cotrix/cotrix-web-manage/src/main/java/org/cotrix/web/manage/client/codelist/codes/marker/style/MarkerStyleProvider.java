/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.style;

import org.cotrix.web.manage.client.codelist.codes.marker.MarkerType;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(DefaultMarkerStyleProvider.class)
public interface MarkerStyleProvider {
	
	public MarkerStyle getStyle(MarkerType type);

}
