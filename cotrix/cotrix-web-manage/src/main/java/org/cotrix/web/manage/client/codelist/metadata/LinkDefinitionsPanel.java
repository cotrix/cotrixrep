package org.cotrix.web.manage.client.codelist.metadata;

import java.util.Collection;

import org.cotrix.web.common.client.factory.UIFactories;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.client.codelist.cache.LinkTypesCache;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.codelist.common.attribute.RemoveItemController;
import org.cotrix.web.manage.client.codelist.event.ReadyEvent;
import org.cotrix.web.manage.client.codelist.metadata.linkdefinition.LinkDefinitionEditingPanelFactory;
import org.cotrix.web.manage.client.codelist.metadata.linkdefinition.LinkDefinitionPanel;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDefinitionsPanel extends Composite implements HasEditing {

	interface LinkDefinitionsPanelUiBinder extends UiBinder<Widget, LinkDefinitionsPanel> {}
	interface LinkDefinitionsPanelEventBinder extends EventBinder<LinkDefinitionsPanel> {}

	private static LinkDefinitionsPanelUiBinder uiBinder = GWT.create(LinkDefinitionsPanelUiBinder.class);

	@UiField FlowPanel itemsContainer;

	@UiField(provided=true) ItemsEditingPanel<UILinkDefinition, LinkDefinitionPanel> linkDefinitionsPanel;

	@UiField HTMLPanel loaderContainer;

	@UiField ItemToolbar toolBar;

	protected DataEditor<UILinkDefinition> linkTypeEditor;

	@Inject
	protected CotrixManagerResources resources;
	
	@Inject
	protected RemoveItemController attributeController;
	
	@Inject
	private LinkDefinitionEditingPanelFactory editingPanelFactory;
	
	@Inject @CurrentCodelist
	private LinkTypesCache linkTypesCache;
	
	@Inject
	private UIFactories factories;

	@Inject
	public void init() {
		linkTypeEditor = DataEditor.build(this);
		
		linkDefinitionsPanel = new ItemsEditingPanel<UILinkDefinition, LinkDefinitionPanel>("Define a link.", editingPanelFactory);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		linkDefinitionsPanel.setListener(new ItemsEditingListener<UILinkDefinition>() {
			
			@Override
			public void onUpdate(UILinkDefinition linkType) {
				linkTypeEditor.updated(linkType);
			}
			
			@Override
			public void onCreate(UILinkDefinition linkType) {
				linkTypeEditor.added(linkType);
			}

			@Override
			public void onSwitch(
					UILinkDefinition item,
					SwitchState state) {
				//ignored				
			}
		});
		
		linkDefinitionsPanel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectionUpdated();
			}
		});
		
		toolBar.addButtonClickedHandler(new ButtonClickedHandler() {

			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case PLUS: addNewLinkType(); break;
					case MINUS: removeSelectedAttribute(); break;
				}
			}
		});
	}

	@Inject
	void bind(LinkDefinitionsPanelEventBinder binder, @CodelistBus EventBus codelistBus) {
		binder.bindEventHandlers(this, codelistBus);
	}

	@Inject
	protected void bind(@CurrentCodelist String codelistId, FeatureBinder featureBinder)
	{

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				toolBar.setVisible(ItemButton.PLUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				attributeController.setUserCanEdit(active);
				//we animate only if the user obtain the edit permission
				updateRemoveButtonVisibility(active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);
	}
	
	@EventHandler
	void onAttributesUpdated(AttributesUpdatedEvent event) {
		HasAttributes attributedItem = event.getAttributedItem();
		if (attributedItem instanceof UILinkDefinition) {
			Log.trace("updated attribues "+attributedItem);
			UILinkDefinition linkType = (UILinkDefinition) attributedItem;
			linkDefinitionsPanel.synchWithModel(linkType);
			//model already updated on save manager
		}
	}
	
	private void selectionUpdated() {
		attributeController.setItemCanBeRemoved(linkDefinitionsPanel.getSelectedItem()!=null);
		updateRemoveButtonVisibility(false);
	}
	
	private void updateRemoveButtonVisibility(boolean animate) {
		toolBar.setVisible(ItemButton.MINUS, attributeController.canRemove(), animate);
	}

	private void addNewLinkType()
	{
		UILinkDefinition linkType = factories.createLinkType();
		linkDefinitionsPanel.addNewItemPanel(linkType);
	}

	private void removeSelectedAttribute()
	{
		UILinkDefinition selectedLinkType = linkDefinitionsPanel.getSelectedItem();
		if (selectedLinkType!=null) {
			linkDefinitionsPanel.removeItem(selectedLinkType);
			linkTypeEditor.removed(selectedLinkType);
		}
	}

	@EventHandler
	void onReady(ReadyEvent event) {
		loadData();
	}
	
	private void loadData()
	{
		showLoader(true);
		setLinkDefinitions(linkTypesCache.getItems());
		showLoader(false);
	}
	
	private void showLoader(boolean visible) {
		loaderContainer.setVisible(visible);
		itemsContainer.setVisible(!visible);
	}

	private void setLinkDefinitions(Collection<UILinkDefinition> types)
	{
		for (UILinkDefinition linkType:types) {
			linkDefinitionsPanel.addItemPanel(linkType);
		}
	}

	@Override
	public void setEditable(boolean editable) {
		linkDefinitionsPanel.setEditable(editable);
	}
}
