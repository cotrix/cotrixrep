package org.cotrix.web.manage.client.codelist.codes;

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ToggleButtonGroup;
import org.cotrix.web.manage.client.codelist.codes.event.CodeSelectedEvent;
import org.cotrix.web.manage.client.di.CodelistBus;

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
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodesSidePanel extends ResizeComposite {

	interface Binder extends UiBinder<Widget, CodesSidePanel> {}
	interface CodesSidePanelEventBinder extends EventBinder<CodesSidePanel> {}
	
	private static Binder uiBinder = GWT.create(Binder.class);
	
	private ToggleButtonGroup buttonGroup = new ToggleButtonGroup();
	
	@UiField ToggleButton attributesButton;
	@UiField ToggleButton filtersButton;
	@UiField ToggleButton linksButton;
	@UiField ToggleButton markersButton;
	
	@UiField DeckLayoutPanel tools;
	
	@UiField EmptyPanel emptyPanel;
	
	@Inject
	@UiField(provided=true) AttributesPanel attributesPanel;
	
	@UiField FiltersPanel filtersPanel;
	
	@Inject
	@UiField(provided=true) LinksPanel linksPanel;
	
	@Inject
	@UiField(provided=true) MarkersPanel markersPanel;

	@Inject
	private void init() {
		initWidget(uiBinder.createAndBindUi(this));
		
		buttonGroup.addButton(attributesButton);
		buttonGroup.addButton(linksButton);
		buttonGroup.addButton(filtersButton);
		buttonGroup.addButton(markersButton);
		
		buttonGroup.setDown(attributesButton);
		
		showEmptyPanel(true);
	}
	
	@Inject
	protected void bind(CodesSidePanelEventBinder binder, @CodelistBus EventBus codelistBus) {
		binder.bindEventHandlers(this, codelistBus);
	}
	
	@UiHandler({"attributesButton", "filtersButton", "linksButton", "markersButton"})
	protected void onButtonClicked(ClickEvent event)
	{
		showPanel((ToggleButton) event.getSource());
	}
	
	@EventHandler
	void onCodeSelected(CodeSelectedEvent event) {
		showEmptyPanel(event.getCode() == null);
	}
	
	public void showEmptyPanel(boolean visible) {
		
		if (visible) {
			tools.showWidget(emptyPanel);
			buttonGroup.setEnabled(false);
		} else {
			showPanel(buttonGroup.getLastSelected());
			buttonGroup.setEnabled(true);
		}
	}
	
	private void showPanel(ToggleButton button) {
		Widget panel = getPanel(button);
		tools.showWidget(panel);
	}
	
	private Widget getPanel(ToggleButton button) {
		if (button == attributesButton) return attributesPanel;
		if (button == filtersButton) return filtersPanel;
		if (button == linksButton) return linksPanel;
		if (button == markersButton) return markersPanel;
		throw new IllegalArgumentException("Unknwown button "+button);
	}

	public AttributesPanel getAttributesPanel() {
		return attributesPanel;
	}
	
	public HasEditing getLinksPanel() {
		return linksPanel;
	}
}
