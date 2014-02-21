/**
 * 
 */
package org.cotrix.web.share.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ToggleButtonGroup {
	
	List<ToggleButton> buttons = new ArrayList<ToggleButton>();
	
	protected final ClickHandler clickHandler = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			updateButtons(event.getSource());
		}
	};
	
	public void addButton(ToggleButton button) {
		button.addClickHandler(clickHandler);
		buttons.add(button);
	}	
	
	protected void updateButtons(Object source)
	{
		for (ToggleButton button:buttons) button.setDown(source == button);
	}

}
