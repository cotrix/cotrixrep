package org.cotrix.web.manage.client.codelist.codes;

import static org.cotrix.web.manage.client.codelist.common.Icons.*;

import java.util.List;

import org.cotrix.web.common.client.factory.UIFactories;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.HasValue;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.codes.event.CodeSelectedEvent;
import org.cotrix.web.manage.client.codelist.codes.link.LinkEditingPanelFactory;
import org.cotrix.web.manage.client.codelist.codes.link.ValueUpdatedEvent;
import org.cotrix.web.manage.client.codelist.common.RemoveItemController;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.codelist.common.form.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.side.SidePanel;
import org.cotrix.web.manage.client.data.CodeLink;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinksPanel extends LoadingPanel implements HasEditing {

	interface LinksPanelEventBinder extends EventBinder<LinksPanel> {}

	@Inject
	private SidePanel panel;
	
	private ItemsEditingPanel<UILink> linksPanel;

	@Inject
	private ManageServiceAsync service;

	@Inject @CurrentCodelist
	private String codelistId;

	private DataEditor<CodeLink> linkEditor;
	private UICode visualizedCode;

	@Inject
	private CotrixManagerResources resources;
	
	@Inject @CodelistBus
	private EventBus codelistBus;
	
	@Inject
	private LinkEditingPanelFactory editingPanelFactory;
	
	@Inject
	private RemoveItemController linkRemotionController;
	
	@Inject
	private UIFactories factories;

	@Inject
	public void init() {
		linkEditor = DataEditor.build(this);
		linksPanel = new ItemsEditingPanel<UILink>("no links.", editingPanelFactory);
		panel.setContent(linksPanel);
		
		panel.getToolBar().setButtonResource(ItemButton.MINUS, RED_MINUS);
		panel.getToolBar().setButtonResource(ItemButton.PLUS, RED_PLUS);
		
		initWidget(panel);
		
		linksPanel.setListener(new ItemsEditingListener<UILink>() {
			
			@Override
			public void onUpdate(UILink link) {
				Log.trace("onUpdate link: "+link);
				linkEditor.updated(new CodeLink(visualizedCode, link));
			}
			
			@Override
			public void onCreate(UILink link) {
				Log.trace("onCreate link: "+link);
				visualizedCode.addLink(link);
				linkEditor.added(new CodeLink(visualizedCode, link));
			}

			@Override
			public void onSwitch(UILink item, SwitchState state) {
			}
		
		});
		
		linksPanel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				boolean canBeRemoved = visualizedCode!=null && linksPanel.getSelectedItem()!=null;	
				linkRemotionController.setItemCanBeRemoved(canBeRemoved);
				updateRemoveButtonVisibility(false);
			}
		});
		
		panel.getToolBar().addButtonClickedHandler(new ButtonClickedHandler() {

			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case PLUS: addNewLink(); break;
					case MINUS: removeSelectedLink(); break;
				}
			}
		});
	}
	
	@Inject
	protected void bind(LinksPanelEventBinder binder, @CodelistBus EventBus codelistBus) {
		binder.bindEventHandlers(this, codelistBus);
	}

	@Inject
	protected void bind(@CurrentCodelist String codelistId, FeatureBinder featureBinder)
	{

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				panel.getToolBar().setEnabled(ItemButton.PLUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);

		featureBinder.bind(new FeatureToggler() {

			@Override
			public void toggleFeature(boolean active) {
				linkRemotionController.setUserCanEdit(active);
				//we animate only if the user obtain the edit permission
				updateRemoveButtonVisibility(active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);
	}
	
	@EventHandler
	void onAttributesUpdated(AttributesUpdatedEvent event) {
		HasAttributes attributedItem = event.getAttributedItem();
		Log.trace("attributes updated for "+attributedItem);
		if (attributedItem instanceof UILink) {
			UILink link = (UILink) attributedItem;
			linksPanel.synchWithModel(link);
			//model already updated on save manager
		}
	}

	@EventHandler
	void onValueUpdated(ValueUpdatedEvent event) {
		HasValue valuedItem = event.getHasValue();
		Log.trace("value updated for "+valuedItem);
		if (valuedItem instanceof UILink) {
			UILink link = (UILink) valuedItem;
			linksPanel.synchWithModel(link);
			//model already updated on save manager
		}
	}
	
	private void updateRemoveButtonVisibility(boolean animate) {
		panel.getToolBar().setEnabled(ItemButton.MINUS, linkRemotionController.canRemove(), animate);
	}
	
	private void addNewLink()
	{
		if (visualizedCode!=null) {
			UILink link = factories.createLink();
			linksPanel.addNewItemPanel(link);
		}
	}

	private void removeSelectedLink()
	{
		UILink selectedLink = linksPanel.getSelectedItem();
		if (selectedLink!=null && visualizedCode!=null) {
			visualizedCode.removeLink(selectedLink);
			linksPanel.removeItem(selectedLink);
			linkEditor.removed(new CodeLink(visualizedCode, selectedLink));
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
	
	@EventHandler
	void onCodeSelected(CodeSelectedEvent event) {
		if (event.getCode() == null) clearVisualizedCode();
		else updateVisualizedCode(event.getCode());

	}
	
	private void updateVisualizedCode(UICode visualizedCode) {
		this.visualizedCode = visualizedCode;
		setLinks(visualizedCode.getLinks());
		updateHeader();
	}
	
	private void clearVisualizedCode()
	{
		visualizedCode = null;
		updateHeader();
		updateBackground();

		linksPanel.clear();
	}
	
	private void updateHeader() {
		panel.setHeader("Links", visualizedCode!=null?visualizedCode.getName().getLocalPart():null, resources.definitions().LOGBOOK_RED());
	}
	
	private void updateBackground()
	{
		setStyleName(CotrixManagerResources.INSTANCE.css().noItemsBackground(), visualizedCode == null || visualizedCode.getAttributes().isEmpty());
	}

	private void setLinks(List<UILink> links)
	{
		linksPanel.clear();
		for (UILink link:links) {
			linksPanel.addItemPanel(link);
		}
	}


	@Override
	public void setEditable(boolean editable) {
		linksPanel.setEditable(editable);
	}
}
