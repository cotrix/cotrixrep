/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;

import java.util.List;

import org.cotrix.web.common.shared.codelist.UIAttribute;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MarkerBinder {
	
	public void bind(List<UIAttribute> attributes, MarkerPanel markerPanel);
	

}
