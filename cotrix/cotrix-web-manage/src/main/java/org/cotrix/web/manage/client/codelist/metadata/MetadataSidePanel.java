package org.cotrix.web.manage.client.codelist.metadata;

import org.cotrix.web.common.client.widgets.HasEditing;
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
	@UiField ToggleButton linkTypesButton;
	@UiField ToggleButton attributeTypesButton;

	
	@UiField DeckLayoutPanel tools;
	
	@Inject
	@UiField(provided=true) AttributesPanel attributesPanel;
	
	@UiField UserPreferencesPanel userPanel;
	
	@Inject
	@UiField(provided=true) LinkTypesPanel linkTypesPanel;
	
	@Inject
	@UiField(provided=true) AttributeTypesPanel attributeTypesPanel;

	@Inject
	private void init() {
		initWidget(uiBinder.createAndBindUi(this));
		tools.showWidget(attributesPanel);

		
		buttonGroup.addButton(metadataButton);
		buttonGroup.addButton(userButton);
		buttonGroup.addButton(linkTypesButton);
		buttonGroup.addButton(attributeTypesButton);
		buttonGroup.setDown(metadataButton);
	}
	
	
	@UiHandler({"metadataButton", "userButton","linkTypesButton", "attributeTypesButton"})
	protected void onButtonClicked(ClickEvent event)
	{
		Widget panel = getPanel((ToggleButton) event.getSource());
		tools.showWidget(panel);
	}
	
	private Widget getPanel(ToggleButton button) {
		if (button == metadataButton) return attributesPanel;
		if (button == userButton) return userPanel;
		if (button == linkTypesButton) return linkTypesPanel;
		if (button == attributeTypesButton) return attributeTypesPanel;
		throw new IllegalArgumentException("Unknwown button "+button);
	}

	public AttributesPanel getAttributesPanel() {
		return attributesPanel;
	}

	public HasEditing getLinkTypesPanel() {
		return linkTypesPanel;
	}
	
	public HasEditing getAttributeTypesPanel() {
		return attributeTypesPanel;
	}
}
