/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.tasks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TaskPanel extends Composite implements HasClickHandlers {

	private static TaskPanelUiBinder uiBinder = GWT.create(TaskPanelUiBinder.class);

	interface TaskPanelUiBinder extends UiBinder<Widget, TaskPanel> {
	}

	@UiField Label name;
	@UiField PushButton button;
	@UiField Label description;
	
	public TaskPanel(String name, String description) {
		initWidget(uiBinder.createAndBindUi(this));
		this.name.setText(name);
		this.description.setText(description);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return button.addClickHandler(handler);
	}
	
	public void setEnabled(boolean enabled) {
		button.setEnabled(enabled);
	}

}
