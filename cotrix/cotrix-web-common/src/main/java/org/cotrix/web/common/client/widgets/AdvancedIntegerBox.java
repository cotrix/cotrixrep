/**
 * 
 */
package org.cotrix.web.common.client.widgets;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.ui.IntegerBox;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AdvancedIntegerBox extends IntegerBox {
	
	public void setPlaceholder(String placeholder)
    {
        InputElement inputElement = getElement().cast();
        inputElement.setAttribute("placeholder", placeholder);
    }
}
