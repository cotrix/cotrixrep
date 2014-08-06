package org.cotrix.web.manage.client.codelist.codes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.cotrix.web.manage.client.codelist.codes.event.GroupSwitchType;
import org.cotrix.web.manage.client.codelist.codes.event.GroupSwitchedEvent;
import org.cotrix.web.manage.client.codelist.codes.event.SwitchGroupEvent;
import org.cotrix.web.manage.client.codelist.codes.link.LinkEditingPanelFactory;
import org.cotrix.web.manage.client.codelist.codes.link.ValueUpdatedEvent;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.codelist.common.form.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.form.ItemsEditingPanel.ItemsEditingListener.SwitchState;
import org.cotrix.web.manage.client.codelist.common.side.SidePanel;
import org.cotrix.web.manage.client.data.CodeLink;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.shared.Group;
import org.cotrix.web.manage.shared.LinkGroup;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
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
	protected ManageServiceAsync service;

	@Inject @CurrentCodelist
	protected String codelistId;

	protected DataEditor<CodeLink> linkEditor;
	protected UICode visualizedCode;

	@Inject
	protected CotrixManagerResources resources;
	
	@Inject @CodelistBus
	protected EventBus codelistBus;
	
	protected Set<LinkGroup> groupsAsColumn;
	
	@Inject
	private LinkEditingPanelFactory editingPanelFactory;
	
	@Inject
	private UIFactories factories;

	@Inject
	public void init() {
		linkEditor = DataEditor.build(this);
		linksPanel = new ItemsEditingPanel<UILink>("no links", editingPanelFactory);
		panel.setContent(linksPanel);
		
		initWidget(panel);
		
		groupsAsColumn = new HashSet<LinkGroup>();
		
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
			public void onSwitch(UILink item,
					SwitchState state) {
				groupSwitch(item, state);
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
				panel.getToolBar().setEnabled(ItemButton.MINUS, active);
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
			if (linkInGroup(link)) linksPanel.setSwitchState(link, SwitchState.DOWN);
		}
	}
	
	private boolean linkInGroup(UILink link) {
		for (LinkGroup group:groupsAsColumn) if (group.accept(visualizedCode.getLinks(), link)) return true;
		return false;
	}

	@Override
	public void setEditable(boolean editable) {
		linksPanel.setEditable(editable);
	}
	
	private void groupSwitch(UILink link, SwitchState state) {
		LinkGroup group = new LinkGroup(link.getDefinitionName(), false);
		group.calculatePosition(visualizedCode.getLinks(), link);
		
		//updateGroups(group, state);

		switch (state) {
			case UP: codelistBus.fireEvent(new SwitchGroupEvent(group, GroupSwitchType.TO_NORMAL));break;
			case DOWN: codelistBus.fireEvent(new SwitchGroupEvent(group, GroupSwitchType.TO_COLUMN)); break;
		}
	}
	
	@EventHandler
	void onGroupSwitched(GroupSwitchedEvent event) {
		Log.trace("onGroupSwitched event: "+event);
		Group group = event.getGroup();

		if (group instanceof LinkGroup) {
			LinkGroup linkGroup = (LinkGroup) group;
			updateGroups(linkGroup, event.getSwitchType());
			if (visualizedCode!=null && linkGroup.match(visualizedCode.getLinks())!=null) refreshSwitches();
		}
	
	}
	
	private void updateGroups(LinkGroup group, GroupSwitchType state) {
		Log.trace("before groups: "+groupsAsColumn);
		switch (state) {
			case TO_NORMAL: groupsAsColumn.remove(group); break;
			case TO_COLUMN: groupsAsColumn.add(group); break;
		}
		Log.trace("after groups: "+groupsAsColumn);
	}
	
	private void refreshSwitches() {
		Log.trace("refreshSwitches");
		if (visualizedCode == null) return;
		for (UILink link:visualizedCode.getLinks()) {
			linksPanel.setSwitchState(link, linkInGroup(link)?SwitchState.DOWN:SwitchState.UP);
		}
	}
}
