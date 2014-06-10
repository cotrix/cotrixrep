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
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.codelist.cache.LinkTypesCache;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.codelist.common.attribute.RemoveItemController;
import org.cotrix.web.manage.client.codelist.metadata.linktype.LinkTypeEditingPanelFactory;
import org.cotrix.web.manage.client.codelist.metadata.linktype.LinkTypePanel;
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
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypesPanel extends LoadingPanel implements HasEditing {

	interface LinkTypesPanelUiBinder extends UiBinder<Widget, LinkTypesPanel> {}
	interface LinkTypesPanelEventBinder extends EventBinder<LinkTypesPanel> {}

	private static LinkTypesPanelUiBinder uiBinder = GWT.create(LinkTypesPanelUiBinder.class);

	@UiField(provided=true) ItemsEditingPanel<UILinkType, LinkTypePanel> linkTypesPanel;

	@UiField ItemToolbar toolBar;

	protected DataEditor<UILinkType> linkTypeEditor;

	@Inject
	protected CotrixManagerResources resources;
	
	@Inject
	protected RemoveItemController attributeController;
	
	@Inject
	private LinkTypeEditingPanelFactory editingPanelFactory;
	
	@Inject @CurrentCodelist
	private LinkTypesCache linkTypesCache;
	
	@Inject
	private UIFactories factories;

	@Inject
	public void init() {
		linkTypeEditor = DataEditor.build(this);
		
		linkTypesPanel = new ItemsEditingPanel<UILinkType, LinkTypePanel>("No links.", editingPanelFactory);
		
		add(uiBinder.createAndBindUi(this));
		
		linkTypesPanel.setListener(new ItemsEditingListener<UILinkType>() {
			
			@Override
			public void onUpdate(UILinkType linkType) {
				linkTypeEditor.updated(linkType);
			}
			
			@Override
			public void onCreate(UILinkType linkType) {
				linkTypeEditor.added(linkType);
			}

			@Override
			public void onSwitch(
					UILinkType item,
					SwitchState state) {
				//ignored				
			}
		});
		
		linkTypesPanel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
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
	void bind(LinkTypesPanelEventBinder binder, @CodelistBus EventBus codelistBus) {
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
				attributeController.setUserCanEdit(active);
				//we animate only if the user obtain the edit permission
				updateRemoveButtonVisibility(active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);
	}
	
	@EventHandler
	void onAttributesUpdated(AttributesUpdatedEvent event) {
		HasAttributes attributedItem = event.getAttributedItem();
		if (attributedItem instanceof UILinkType) {
			Log.trace("updated attribues "+attributedItem);
			UILinkType linkType = (UILinkType) attributedItem;
			linkTypesPanel.synchWithModel(linkType);
			//model already updated on save manager
		}
	}
	
	private void selectionUpdated() {
		attributeController.setItemCanBeRemoved(linkTypesPanel.getSelectedItem()!=null);
		updateRemoveButtonVisibility(false);
	}
	
	private void updateRemoveButtonVisibility(boolean animate) {
		toolBar.setVisible(ItemButton.MINUS, attributeController.canRemove(), animate);
	}

	private void addNewLinkType()
	{
		UILinkType linkType = factories.createLinkType();
		linkTypesPanel.addNewItemPanel(linkType);
	}

	private void removeSelectedAttribute()
	{
		UILinkType selectedLinkType = linkTypesPanel.getSelectedItem();
		if (selectedLinkType!=null) {
			linkTypesPanel.removeItem(selectedLinkType);
			linkTypeEditor.removed(selectedLinkType);
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
		linkTypesCache.getItems(new AsyncCallback<Collection<UILinkType>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading CodelistLinkTypes", caught);
				hideLoader();
			}

			@Override
			public void onSuccess(Collection<UILinkType> result) {
				Log.trace("retrieved CodelistLinkTypes: "+result);
				setLinkTypes(result);
				hideLoader();
			}
		});
	}

	private void setLinkTypes(Collection<UILinkType> types)
	{
		for (UILinkType linkType:types) {
			linkTypesPanel.addItemPanel(linkType);
		}
	}

	@Override
	public void setEditable(boolean editable) {
		linkTypesPanel.setEditable(editable);
	}
}
