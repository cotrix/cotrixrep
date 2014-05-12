/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AdvancedTextBox extends TextBox {
	
	public void setPlaceholder(String placeholder)
    {
        InputElement inputElement = getElement().cast();
        inputElement.setAttribute("placeholder", placeholder);
    }
}
