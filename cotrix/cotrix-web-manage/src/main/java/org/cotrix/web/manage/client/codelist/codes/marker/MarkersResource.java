/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker;

import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.resources.client.CssResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MarkersResource extends CssResource {
	
	public static final MarkersResource style = CotrixManagerResources.INSTANCE.markers();
	
	String deletedBackgroundColor();
	String deletedTextColor();
	String deletedHighlight();
	
	
	String invalidBackgroundColor();
	String invalidTextColor();
	String invalidHighlight();
	
	
	String anotherMarkerBackgroundColor();
	String anotherMarkerTextColor();
	String anotherMarkerHighlight();
	
	String newCodeBackgroundColor();
	String newCodeTextColor();
	String newCodeHighlight();

	String modifiedBackgroundColor();
	String modifiedTextColor();
	String modifiedHighlight();
	
	String toolbarButton();
	String selectedButtonColor();
	
}
