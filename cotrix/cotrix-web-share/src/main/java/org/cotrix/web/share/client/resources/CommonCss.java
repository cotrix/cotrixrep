/**
 * 
 */
package org.cotrix.web.share.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface CommonCss extends CssResource {


	@ClassName("gwt-PopupPanelGlass")
	String glassPanel();

	String simpleCheckbox();
	
	String paddedText();
	String paddedTextDisabled();
	
	String linkText();	
	
	String listBox();
	
	String imageButton();
	
	String textBox();
	
	String verticalHeadersSingleItem();
	
	String verticalHeadersMultipleItem();
	
	String verticalHeadersFieldItem();
	
	String blueButton();
	String grayButton();

	String propertyLabel();
	String propertyValue();
	
	String propertiesTable();
	String propertiesTableHeader();
}
