package org.cotrix.web.manage.client.codelist;

import java.util.List;

import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.DataWindow;
import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.attribute.RemoveItemController;
import org.cotrix.web.manage.client.codelist.attribute.event.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.linktype.LinkTypeEditingPanelFactory;
import org.cotrix.web.manage.client.codelist.linktype.LinkTypePanel;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Constants;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
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
public class CodelistLinkTypesPanel extends LoadingPanel implements HasEditing {

	@UiTemplate("CodelistLinkTypesPanel.ui.xml")
	interface CodelistLinkTypesPanelUiBinder extends UiBinder<Widget, CodelistLinkTypesPanel> {}
	interface CodelistLinkTypesPanelEventBinder extends EventBinder<CodelistLinkTypesPanel> {}

	private static CodelistLinkTypesPanelUiBinder uiBinder = GWT.create(CodelistLinkTypesPanelUiBinder.class);

	@UiField(provided=true) ItemsEditingPanel<UILinkType, LinkTypePanel> linkTypesPanel;

	@UiField ItemToolbar toolBar;

	protected boolean dataLoaded = false;;

	@Inject
	protected ManageServiceAsync service;

	@Inject @CurrentCodelist
	protected String codelistId;

	protected DataEditor<UILinkType> linkTypeEditor;

	@Inject
	protected Constants constants;

	@Inject
	protected CotrixManagerResources resources;
	
	@Inject
	protected RemoveItemController attributeController;
	
	@Inject
	private LinkTypeEditingPanelFactory editingPanelFactory;

	@Inject
	public void init() {
		linkTypeEditor = DataEditor.build(this);
		
		linkTypesPanel = new ItemsEditingPanel<UILinkType, LinkTypePanel>("Codelist Links", "no links", editingPanelFactory);
		
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
					case PLUS: addNewAttribute(); break;
					case MINUS: removeSelectedAttribute(); break;
				}
			}
		});
	}

	@Inject
	void bind(CodelistLinkTypesPanelEventBinder binder, @EditorBus EventBus editorBus) {
		binder.bindEventHandlers(this, editorBus);
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

	private void addNewAttribute()
	{
		UILinkType linkType = new UILinkType();
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

		if (!dataLoaded) loadData();
	}

	public void loadData()
	{
		showLoader();
		service.getCodelistLinkTypes(codelistId, new AsyncCallback<DataWindow<UILinkType>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading CodelistLinkTypes", caught);
				hideLoader();
			}

			@Override
			public void onSuccess(DataWindow<UILinkType> result) {
				Log.trace("retrieved CodelistLinkTypes: "+result);
				setLinkTypes(result.getData());
				hideLoader();
			}
		});
	}

	private void setLinkTypes(List<UILinkType> types)
	{
		for (UILinkType linkType:types) {
			linkTypesPanel.addItemPanel(linkType);
		}
		dataLoaded = true;
	}

	@Override
	public void setEditable(boolean editable) {
		linkTypesPanel.setEditable(editable);
	}
}
