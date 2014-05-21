/**
 * 
 */
package org.cotrix.web.common.client.widgets;

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
	
	private List<ToggleButton> buttons = new ArrayList<ToggleButton>();
	private ToggleButton lastSelected;
	
	private final ClickHandler clickHandler = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			setDown((ToggleButton) event.getSource());
		}
	};
	
	public void setDown(ToggleButton button) {
		updateButtons(button);
		lastSelected = button;
	}
	
	public void addButton(ToggleButton button) {
		button.addClickHandler(clickHandler);
		buttons.add(button);
	}	
	
	private void updateButtons(Object source)
	{
		for (ToggleButton button:buttons) button.setDown(source == button);
	}
	
	public void setEnabled(boolean enabled) {
		for (ToggleButton button:buttons) button.setEnabled(enabled);
	}
	
	public ToggleButton getLastSelected() {
		return lastSelected;
	}

}
