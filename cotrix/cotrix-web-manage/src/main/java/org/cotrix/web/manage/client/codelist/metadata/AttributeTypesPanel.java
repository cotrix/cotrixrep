package org.cotrix.web.manage.client.codelist.metadata;

import java.util.List;

import org.cotrix.web.common.client.factory.UIFactories;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.attribute.RemoveItemController;
import org.cotrix.web.manage.client.codelist.metadata.attributetype.AttributeTypeEditingPanelFactory;
import org.cotrix.web.manage.client.codelist.metadata.attributetype.AttributeTypePanel;
import org.cotrix.web.manage.client.codelist.provider.AttributeTypesCache;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypesPanel extends LoadingPanel implements HasEditing {

	interface AttributeTypesPanelUiBinder extends UiBinder<Widget, AttributeTypesPanel> {}
	interface AttributeTypesPanelEventBinder extends EventBinder<AttributeTypesPanel> {}

	private static AttributeTypesPanelUiBinder uiBinder = GWT.create(AttributeTypesPanelUiBinder.class);

	@UiField(provided=true) ItemsEditingPanel<UIAttributeType, AttributeTypePanel> attributeTypesPanel;

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
	public void init() {
		attributeTypeEditor = DataEditor.build(this);
		
		attributeTypesPanel = new ItemsEditingPanel<UIAttributeType, AttributeTypePanel>("no definitions", editingPanelFactory);
		
		add(uiBinder.createAndBindUi(this));
		
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
	protected void bind(@CurrentCodelist String codelistId)
	{

		FeatureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				toolBar.setVisible(ItemButton.PLUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);

		FeatureBinder.bind(new FeatureToggler() {

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
		UIAttributeType selectedAttributeType = attributeTypesPanel.getSelectedItem();
		if (selectedAttributeType!=null) {
			attributeTypesPanel.removeItem(selectedAttributeType);
			attributeTypeEditor.removed(selectedAttributeType);
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		//workaround issue #7188 https://code.google.com/p/google-web-toolkit/issues/detail?id=7188
		onResize();
	}

	public void loadData()
	{
		showLoader();
		attributeTypesCache.getItems(new AsyncCallback<List<UIAttributeType>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading CodelistAttributeTypes", caught);
				hideLoader();
			}

			@Override
			public void onSuccess(List<UIAttributeType> result) {
				Log.trace("retrieved CodelistAttributeTypes: "+result);
				setAttributeTypes(result);
				hideLoader();
			}
		});
	}

	private void setAttributeTypes(List<UIAttributeType> types)
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
