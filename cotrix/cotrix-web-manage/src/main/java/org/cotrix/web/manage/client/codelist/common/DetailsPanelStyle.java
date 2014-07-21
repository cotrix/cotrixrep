/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common;

import com.google.gwt.resources.client.CssResource;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface DetailsPanelStyle extends CssResource {
	
	String table();
	
	String headerCell();
	String valueCell();
	
	String valueCellLeft();
	String valueCellCenter();
	String valueCellRight();
	
	String textbox();
	String textboxError();
	String listbox();
	String textarea();
	
	String loaderContainer();
	String loader();
	
	String suggestionbox();
	String suggestionboxTextbox();
	String suggestionboxButton();
	String suggestionPopup();
	String suggestionItem();
	String suggestionItemSelected();
	
	String label();

}
