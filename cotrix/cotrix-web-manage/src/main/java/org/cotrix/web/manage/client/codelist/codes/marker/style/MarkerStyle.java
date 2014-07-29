/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.marker.style;

import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyleResource.Style;
import org.cotrix.web.manage.client.codelist.codes.marker.style.MarkerStyleResource.StyleElement;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MarkerStyle {
	
	@Style(StyleElement.BACKGROUND_COLOR)
	public String getBackgroundColor();
	
	@Style(StyleElement.TEXT_COLOR)
	public String getTextColor();
	
	@Style(StyleElement.HIGHLIGHT)
	public String getHighlightStyleName();

}
