/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.tasks;

import org.cotrix.web.common.client.widgets.button.ButtonResources;
import org.cotrix.web.manage.client.codelist.common.header.HeaderPanel;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;

import static org.cotrix.web.common.client.widgets.button.ButtonResourceBuilder.*;
import static org.cotrix.web.manage.client.codelist.common.Icons.icons;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TaskPanel extends Composite {

	private static ButtonResources PLAY = create().upFace(icons.play()).hover(icons.playHover()).disabled(icons.playDisabled()).title("Make changes.").build();
	
	private HeaderPanel panel;
	
	public TaskPanel(String name, String description) {
		panel = new HeaderPanel();
		panel.setIcon(icons.task(), icons.taskDisabled());
		panel.setPrimaryButton(PLAY);
		panel.setPrimaryButtonVisible(true);
		
		initWidget(panel);
		setWidth("100%");
		
		panel.setTitle(name);
		panel.setSubtitle(description);
	}

	public void addClickHandler(ClickHandler handler) {
		panel.addPrimaryButtonClickHandler(handler);
	}
	
	public void setEnabled(boolean enabled) {
		panel.setDisabled(!enabled);
	}

}
