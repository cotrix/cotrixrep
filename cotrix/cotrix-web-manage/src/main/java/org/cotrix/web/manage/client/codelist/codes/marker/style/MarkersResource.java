/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.style;

import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import static org.cotrix.web.manage.client.codelist.codes.marker.MarkerType.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MarkersResource extends MarkerStyleResource {
	
	public static final MarkersResource INSTANCE = CotrixManagerResources.INSTANCE.markers();
	
	@BackgroundColor(DELETED)
	String deletedBackgroundColor();
	@TextColor(DELETED)
	String deletedTextColor();
	@Highlight(DELETED)
	String deletedHighlight();
	
	@BackgroundColor(INVALID)
	String invalidBackgroundColor();
	@TextColor(INVALID)
	String invalidTextColor();
	@Highlight(INVALID)
	String invalidHighlight();
	
	@BackgroundColor(NEWCODE)
	String newCodeBackgroundColor();
	@TextColor(NEWCODE)
	String newCodeTextColor();
	@Highlight(NEWCODE)
	String newCodeHighlight();

	@BackgroundColor(MODIFIED)
	String modifiedBackgroundColor();
	@TextColor(MODIFIED)
	String modifiedTextColor();
	@Highlight(MODIFIED)
	String modifiedHighlight();
	
	String toolbarButton();
	String selectedButtonColor();
	
	String menuItem();
	String menuItemSelected();
	
}
