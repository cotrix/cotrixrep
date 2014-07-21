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
import org.cotrix.web.common.client.widgets.dialog.ConfirmDialog.DialogButtonDefaultSet;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.client.codelist.cache.AttributeTypesCache;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.attribute.RemoveItemController;
import org.cotrix.web.manage.client.codelist.event.ReadyEvent;
import org.cotrix.web.manage.client.codelist.metadata.attributetype.AttributeTypeEditingPanelFactory;
import org.cotrix.web.manage.client.codelist.metadata.attributetype.AttributeTypePanel;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.shared.ManagerUIFeature;

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
public class AttributeTypesPanel extends Composite implements HasEditing {

	interface AttributeTypesPanelUiBinder extends UiBinder<Widget, AttributeTypesPanel> {}
	interface AttributeTypesPanelEventBinder extends EventBinder<AttributeTypesPanel> {}

	private static AttributeTypesPanelUiBinder uiBinder = GWT.create(AttributeTypesPanelUiBinder.class);

	@UiField FlowPanel itemsContainer;
	
	@UiField(provided=true) ItemsEditingPanel<UIAttributeType, AttributeTypePanel> attributeTypesPanel;

	@UiField HTMLPanel loaderContainer;
	
	@UiField ItemToolbar toolBar;

	private DataEditor<UIAttributeType> attributeTypeEditor;

	@Inject
	private CotrixManagerResources resources;
	
	@Inject
	private RemoveItemController attributeTypeController;
	
	@Inject
	private AttributeTypeEditingPanelFactory editingPanelFactory;
	
	@Inject @CurrentCodelist
	private AttributeTypesCache attributeTypesCache;
	
	@Inject
	private UIFactories factories;
	
	@Inject
	private ConfirmDialog confirmDialog;

	@Inject
	public void init() {
		attributeTypeEditor = DataEditor.build(this);
		
		attributeTypesPanel = new ItemsEditingPanel<UIAttributeType, AttributeTypePanel>("Define an attribute.", editingPanelFactory);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		attributeTypesPanel.setListener(new ItemsEditingListener<UIAttributeType>() {
			
			@Override
			public void onUpdate(UIAttributeType attributeType) {
				attributeTypeEditor.updated(attributeType);
			}
			
			@Override
			public void onCreate(UIAttributeType attributeType) {
				attributeTypeEditor.added(attributeType);
			}

			@Override
			public void onSwitch(UIAttributeType item, SwitchState state) {
				//ignored				
			}
		});
		
		attributeTypesPanel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
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
	void bind(AttributeTypesPanelEventBinder binder, @CodelistBus EventBus codelistBus) {
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
		attributeTypeController.setItemCanBeRemoved(attributeTypesPanel.getSelectedItem()!=null);
		updateRemoveButtonVisibility(false);
	}
	
	private void updateRemoveButtonVisibility(boolean animate) {
		toolBar.setVisible(ItemButton.MINUS, attributeTypeController.canRemove(), animate);
	}

	private void addNewAttributeType()
	{
		UIAttributeType attributeType = factories.createAttributeType();
		attributeTypesPanel.addNewItemPanel(attributeType);
	}

	private void removeSelectedAttributeType()
	{
		final UIAttributeType selectedAttributeType = attributeTypesPanel.getSelectedItem();
		if (selectedAttributeType!=null) {
			
			confirmDialog.center("This will delete all the instances of this attribute|link.<br>Do you want to go ahead?", new ConfirmDialogListener() {
				
				@Override
				public void onButtonClick(DialogButton button) {
					if (button == DialogButtonDefaultSet.CONTINUE) {
						attributeTypesPanel.removeItem(selectedAttributeType);
						attributeTypeEditor.removed(selectedAttributeType);
					}
				}
			});
			
			
		}
	}
	
	@EventHandler
	void onReady(ReadyEvent event) {
		loadData();
	}

	private void loadData()
	{
		showLoader(true);
		setAttributeTypes(attributeTypesCache.getItems());
		showLoader(false);
	}
	
	private void showLoader(boolean visible) {
		loaderContainer.setVisible(visible);
		itemsContainer.setVisible(!visible);
	}

	private void setAttributeTypes(Collection<UIAttributeType> types)
	{
		for (UIAttributeType attributeType:types) {
			attributeTypesPanel.addItemPanel(attributeType);
		}
	}

	@Override
	public void setEditable(boolean editable) {
		attributeTypesPanel.setEditable(editable);
	}
}
