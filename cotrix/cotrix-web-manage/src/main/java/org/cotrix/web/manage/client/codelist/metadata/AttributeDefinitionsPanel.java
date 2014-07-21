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
import org.cotrix.web.common.client.widgets.dialog.ConfirmDialog;
import org.cotrix.web.common.client.widgets.dialog.ConfirmDialog.ConfirmDialogListener;
import org.cotrix.web.common.client.widgets.dialog.ConfirmDialog.DialogButton;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.client.codelist.cache.AttributeDefinitionsCache;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.attribute.RemoveItemController;
import org.cotrix.web.manage.client.codelist.metadata.attributedefinition.AttributeDefinitionEditingPanelFactory;
import org.cotrix.web.manage.client.codelist.metadata.attributedefinition.AttributeDefinitionPanel;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitionsPanel extends Composite implements HasEditing {

	interface AttributeDefinitionsPanelUiBinder extends UiBinder<Widget, AttributeDefinitionsPanel> {}
	interface AttributeDefinitionsPanelEventBinder extends EventBinder<AttributeDefinitionsPanel> {}

	private static AttributeDefinitionsPanelUiBinder uiBinder = GWT.create(AttributeDefinitionsPanelUiBinder.class);

	@UiField FlowPanel itemsContainer;
	
	@UiField(provided=true) ItemsEditingPanel<UIAttributeDefinition, AttributeDefinitionPanel> attributeDefinitionsPanel;

	@UiField HTMLPanel loaderContainer;
	
	@UiField ItemToolbar toolBar;

	private DataEditor<UIAttributeDefinition> attributeTypeEditor;

	@Inject
	private CotrixManagerResources resources;
	
	@Inject
	private RemoveItemController attributeTypeController;
	
	@Inject
	private AttributeDefinitionEditingPanelFactory editingPanelFactory;
	
	@Inject @CurrentCodelist
	private AttributeDefinitionsCache attributeTypesCache;
	
	@Inject
	private UIFactories factories;
	
	@Inject
	private ConfirmDialog confirmDialog;

	@Inject
	public void init() {
		attributeTypeEditor = DataEditor.build(this);
		
		attributeDefinitionsPanel = new ItemsEditingPanel<UIAttributeDefinition, AttributeDefinitionPanel>("Define an attribute.", editingPanelFactory);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		attributeDefinitionsPanel.setListener(new ItemsEditingListener<UIAttributeDefinition>() {
			
			@Override
			public void onUpdate(UIAttributeDefinition attributeType) {
				attributeTypeEditor.updated(attributeType);
			}
			
			@Override
			public void onCreate(UIAttributeDefinition attributeType) {
				attributeTypeEditor.added(attributeType);
			}

			@Override
			public void onSwitch(UIAttributeDefinition item, SwitchState state) {
				//ignored				
			}
		});
		
		attributeDefinitionsPanel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectionUpdated();
			}
		});
		
		toolBar.addButtonClickedHandler(new ButtonClickedHandler() {

			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case PLUS: addNewAttributeType(); break;
					case MINUS: removeSelectedAttributeType(); break;
				}
			}
		});
	}

	@Inject
	void bind(AttributeDefinitionsPanelEventBinder binder, @CodelistBus EventBus codelistBus) {
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
				attributeTypeController.setUserCanEdit(active);
				//we animate only if the user obtain the edit permission
				updateRemoveButtonVisibility(active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);
	}
	
	private void selectionUpdated() {
		attributeTypeController.setItemCanBeRemoved(attributeDefinitionsPanel.getSelectedItem()!=null);
		updateRemoveButtonVisibility(false);
	}
	
	private void updateRemoveButtonVisibility(boolean animate) {
		toolBar.setVisible(ItemButton.MINUS, attributeTypeController.canRemove(), animate);
	}

	private void addNewAttributeType()
	{
		UIAttributeDefinition attributeType = factories.createAttributeType();
		attributeDefinitionsPanel.addNewItemPanel(attributeType);
	}

	private void removeSelectedAttributeType()
	{
		final UIAttributeDefinition selectedAttributeType = attributeDefinitionsPanel.getSelectedItem();
		if (selectedAttributeType!=null) {
			
			confirmDialog.center("This will delete all the instances of this attribute|link.<br>Do you want to go ahead?", new ConfirmDialogListener() {
				
				@Override
				public void onButtonClick(DialogButton button) {
					if (button == DialogButton.CONTINUE) {
						attributeDefinitionsPanel.removeItem(selectedAttributeType);
						attributeTypeEditor.removed(selectedAttributeType);
					}
				}
			});
			
			
		}
	}

	public void loadData()
	{
		showLoader(true);
		attributeTypesCache.getItems(new AsyncCallback<Collection<UIAttributeDefinition>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading CodelistAttributeTypes", caught);
				showLoader(false);
			}

			@Override
			public void onSuccess(Collection<UIAttributeDefinition> result) {
				Log.trace("retrieved CodelistAttributeTypes: "+result);
				setAttributeTypes(result);
				showLoader(false);
			}
		});
	}
	
	private void showLoader(boolean visible) {
		loaderContainer.setVisible(visible);
		itemsContainer.setVisible(!visible);
	}

	private void setAttributeTypes(Collection<UIAttributeDefinition> types)
	{
		for (UIAttributeDefinition attributeType:types) {
			attributeDefinitionsPanel.addItemPanel(attributeType);
		}
	}

	@Override
	public void setEditable(boolean editable) {
		attributeDefinitionsPanel.setEditable(editable);
	}
}
