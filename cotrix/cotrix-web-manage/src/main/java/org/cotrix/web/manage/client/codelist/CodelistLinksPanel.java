package org.cotrix.web.manage.client.codelist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureToggler;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.ManageServiceAsync;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener.SwitchState;
import org.cotrix.web.manage.client.codelist.event.CodeSelectedEvent;
import org.cotrix.web.manage.client.codelist.event.GroupSwitchType;
import org.cotrix.web.manage.client.codelist.event.GroupSwitchedEvent;
import org.cotrix.web.manage.client.codelist.event.SwitchGroupEvent;
import org.cotrix.web.manage.client.codelist.link.LinksPanel;
import org.cotrix.web.manage.client.codelist.link.LinksPanel.LinksPanelListener;
import org.cotrix.web.manage.client.data.CodeLink;
import org.cotrix.web.manage.client.data.DataEditor;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Constants;
import org.cotrix.web.manage.shared.Group;
import org.cotrix.web.manage.shared.LinkGroup;
import org.cotrix.web.manage.shared.ManagerUIFeature;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistLinksPanel extends LoadingPanel implements HasEditing {

	@UiTemplate("CodelistLinksPanel.ui.xml")
	interface CodelistLinksPanelUiBinder extends UiBinder<Widget, CodelistLinksPanel> {}
	interface CodelistLinksPanelEventBinder extends EventBinder<CodelistLinksPanel> {}

	protected static ImageResourceRenderer renderer = new ImageResourceRenderer(); 

	@Inject
	@UiField(provided=true) LinksPanel linksPanel;

	@UiField ItemToolbar toolBar;

	@Inject
	protected ManageServiceAsync service;

	@Inject @CurrentCodelist
	protected String codelistId;

	protected DataEditor<CodeLink> linkEditor;
	protected UICode currentCode;

	@Inject
	protected Constants constants;

	@Inject
	protected CotrixManagerResources resources;
	
	@Inject @EditorBus
	protected EventBus editorBus;
	
	protected Set<LinkGroup> groupsAsColumn;

	@Inject
	public void init(CodelistLinksPanelUiBinder uiBinder) {
		linkEditor = DataEditor.build(this);
		add(uiBinder.createAndBindUi(this));
		
		groupsAsColumn = new HashSet<LinkGroup>();
		
		linksPanel.setListener(new LinksPanelListener() {
			
			@Override
			public void onUpdate(UILink link) {
				Log.trace("onUpdate link: "+link);
				linkEditor.updated(new CodeLink(currentCode, link));
			}
			
			@Override
			public void onCreate(UILink link) {
				Log.trace("onCreate link: "+link);
				currentCode.addLink(link);
				linkEditor.added(new CodeLink(currentCode, link));
			}

			@Override
			public void onSwitch(UILink item,
					SwitchState state) {
				groupSwitch(item, state);
			}
		
		});
		
		toolBar.addButtonClickedHandler(new ButtonClickedHandler() {

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
	protected void bind(CodelistLinksPanelEventBinder binder, @EditorBus EventBus editorBus) {
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
				toolBar.setVisible(ItemButton.MINUS, active);
			}
		}, codelistId, ManagerUIFeature.EDIT_METADATA);
	}
	
	private void addNewLink()
	{
		if (currentCode!=null) linksPanel.addNewLink();
	}

	private void removeSelectedLink()
	{
		UILink selectedLink = linksPanel.getSelectedType();
		if (selectedLink!=null && currentCode!=null) {
			currentCode.removeLink(selectedLink);
			linksPanel.removeLink(selectedLink);
			linkEditor.removed(new CodeLink(currentCode, selectedLink));
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
		currentCode = event.getCode();
		setLinks(currentCode.getLinks());
		String codeName = currentCode!=null?ValueUtils.getLocalPart(currentCode.getName()):null;
		linksPanel.setSelectedCode(codeName);
	}

	private void setLinks(List<UILink> links)
	{
		linksPanel.clear();
		for (UILink link:links) {
			linksPanel.addLink(link);
			if (linkInGroup(link)) linksPanel.setSwitchState(link, SwitchState.DOWN);
		}
	}
	
	private boolean linkInGroup(UILink link) {
		for (LinkGroup group:groupsAsColumn) if (group.accept(currentCode.getLinks(), link)) return true;
		return false;
	}

	@Override
	public void setEditable(boolean editable) {
		linksPanel.setEditable(editable);
	}
	
	private void groupSwitch(UILink link, SwitchState state) {
		LinkGroup group = new LinkGroup(link.getTypeName(), false);
		group.calculatePosition(currentCode.getLinks(), link);
		
		//updateGroups(group, state);

		switch (state) {
			case UP: editorBus.fireEvent(new SwitchGroupEvent(group, GroupSwitchType.TO_NORMAL));break;
			case DOWN: editorBus.fireEvent(new SwitchGroupEvent(group, GroupSwitchType.TO_COLUMN)); break;
		}
	}
	
	@EventHandler
	void onGroupSwitched(GroupSwitchedEvent event) {
		Log.trace("onGroupSwitched event: "+event);
		Group group = event.getGroup();

		if (group instanceof LinkGroup) {
			LinkGroup linkGroup = (LinkGroup) group;
			updateGroups(linkGroup, event.getSwitchType());
			if (currentCode!=null && linkGroup.match(currentCode.getLinks())!=null) refreshSwitches();
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
		if (currentCode == null) return;
		for (UILink link:currentCode.getLinks()) {
			linksPanel.setSwitchState(link, linkInGroup(link)?SwitchState.DOWN:SwitchState.UP);
		}
	}
}
