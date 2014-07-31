package org.cotrix.web.manage.client.codelist.metadata;

import org.cotrix.web.common.client.widgets.ToggleButtonGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataSidePanel extends ResizeComposite {

	interface Binder extends UiBinder<Widget, MetadataSidePanel> { }
	
	private static Binder uiBinder = GWT.create(Binder.class);
	
	private ToggleButtonGroup buttonGroup = new ToggleButtonGroup();
	@UiField ToggleButton metadataButton;
	@UiField ToggleButton userButton;
	@UiField ToggleButton logbookButton;
	@UiField ToggleButton tasksButton;
	
	@UiField DeckLayoutPanel tools;
	
	@Inject
	@UiField(provided=true) AttributesPanel attributesPanel;
	
	@UiField UserPreferencesPanel userPanel;
	
	@Inject
	@UiField(provided=true) LogbookPanel logbookPanel;
	
	@Inject
	@UiField(provided=true) TasksPanel tasksPanel;

	@Inject
	private void init() {
		initWidget(uiBinder.createAndBindUi(this));
		tools.showWidget(attributesPanel);

		buttonGroup.addButton(metadataButton);
		buttonGroup.addButton(userButton);
		buttonGroup.addButton(logbookButton);
		buttonGroup.addButton(tasksButton);
		
		buttonGroup.setDown(metadataButton);
	}
	
	
	@UiHandler({"metadataButton", "userButton", "logbookButton", "tasksButton"})
	protected void onButtonClicked(ClickEvent event)
	{
		Widget panel = getPanel((ToggleButton) event.getSource());
		tools.showWidget(panel);
	}
	
	private Widget getPanel(ToggleButton button) {
		if (button == metadataButton) return attributesPanel;
		if (button == userButton) return userPanel;
		if (button == logbookButton) return logbookPanel;
		if (button == tasksButton) return tasksPanel;
		throw new IllegalArgumentException("Unknwown button "+button);
	}

	public AttributesPanel getAttributesPanel() {
		return attributesPanel;
	}
}
