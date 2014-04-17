/**
 * 
 */
package org.cotrix.web.manage.client.codelist.linktype;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.cotrix.web.common.client.util.InstanceMap;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.codelist.attribute.AttributesGridResources;
import org.cotrix.web.manage.client.codelist.attribute.event.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.codelist.linktype.LinkTypePanel.LinkTypePanelListener;
import org.cotrix.web.manage.client.event.EditorBus;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypesPanel extends Composite implements HasEditing {
	
	public interface LinkTypesPanelListener {
		public void onCreate(UILinkType linkType);
		public void onUpdate(UILinkType linkType);
	}
	
	interface LinkTypesPanelEventBinder extends EventBinder<LinkTypesPanel> {}
	
	private VerticalPanel panel;
	private List<LinkTypePanel> panels = new ArrayList<LinkTypePanel>();
	
	protected static AttributesGridResources gridResource = GWT.create(AttributesGridResources.class);
	private LinkTypePanel currentSelection;
	
	private InstanceMap<UILinkType, LinkTypePanel> instances = new InstanceMap<UILinkType, LinkTypePanel>();
	
	@Inject
	private LinkTypesCodelistInfoProvider codelistInfoProvider;
	
	private LinkTypesPanelListener listener;
	
	public LinkTypesPanel() {
		panel = new VerticalPanel();
		panel.setWidth("100%");
		
		gridResource.dataGridStyle().ensureInjected();
		
		Label header = new Label("Codelist Links");
		header.setStyleName(gridResource.dataGridStyle().dataGridHeader());
		panel.add(header);
		
		initWidget(panel);
	}
	
	@Inject
	protected void bind(LinkTypesPanelEventBinder binder, @EditorBus EventBus editorBus) {
		binder.bindEventHandlers(this, editorBus);
	}
	
	@EventHandler
	void onAttributesUpdated(AttributesUpdatedEvent event) {
		HasAttributes attributedItem = event.getAttributedItem();
		if (attributedItem instanceof UILinkType) {
			Log.trace("updated attribues "+attributedItem);
			UILinkType linkType = (UILinkType) attributedItem;
			LinkTypePanel panel = instances.get(linkType);
			if (panel!=null) panel.syncWithModel();
			//model already updated on save manager
		}
	}

	/**
	 * Used for tests
	 * @param codelistInfoProvider
	 */
	public void setCodelistInfoProvider(LinkTypesCodelistInfoProvider codelistInfoProvider) {
		this.codelistInfoProvider = codelistInfoProvider;
	}

	public void setListener(LinkTypesPanelListener listener) {
		this.listener = listener;
	}
	public void removeLinkType(UILinkType linkType) {
		LinkTypePanel linkTypePanel = instances.remove(linkType);
		if (linkTypePanel == null) return;
		if (currentSelection == linkTypePanel) currentSelection = null;
		panel.remove(linkTypePanel);
		panels.remove(linkTypePanel);
	}
	
	public void addLinkType(UILinkType linkType) {
		final LinkTypePanel linkTypePanel = new LinkTypePanel(linkType, codelistInfoProvider);
		panels.add(linkTypePanel);
		
		instances.put(linkType, linkTypePanel);
		
		linkTypePanel.setListener(new LinkTypePanelListener() {
			
			@Override
			public void onSave(UILinkType linkType) {
				fireUpdate(linkType);
			}
			
			@Override
			public void onCancel() {
				
			}

			@Override
			public void onSelect() {
				updateSelection(linkTypePanel);				
			}
		});
		panel.add(linkTypePanel);
	}
	
	public void addNewLinkType() {
		UILinkType linkType = new UILinkType();
		final LinkTypePanel linkTypePanel = new LinkTypePanel(linkType, codelistInfoProvider);
		panels.add(linkTypePanel);
		
		instances.put(linkType, linkTypePanel);
		
		linkTypePanel.setListener(new LinkTypePanelListener() {
			
			boolean created = false;
			
			@Override
			public void onSave(UILinkType linkType) {
				if (!created) {
					fireCreate(linkType);
					created = true;
				} else fireUpdate(linkType);
			}
			
			@Override
			public void onCancel() {
				if (!created) panel.remove(linkTypePanel);
			}

			@Override
			public void onSelect() {
				updateSelection(linkTypePanel);
			}
		});
		panel.add(linkTypePanel);
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				linkTypePanel.enterEditMode(true);
			}
		});
	}
	
	private void updateSelection(LinkTypePanel selected) {
		System.out.println("selected "+selected);
		if (currentSelection!=null) currentSelection.setSelected(false);
		selected.setSelected(true);
		currentSelection = selected;
	}
	
	public UILinkType getSelectedType() {
		if (currentSelection!=null) return instances.getByValue(currentSelection);
		return null;
	}

	@Override
	public void setEditable(boolean editable) {
		for (LinkTypePanel linkTypePanel:panels) linkTypePanel.setEditable(editable);
	}

	private void fireUpdate(UILinkType linkType) {
		if (listener!=null) listener.onUpdate(linkType);
	}
	
	private void fireCreate(UILinkType linkType) {
		if (listener!=null) listener.onCreate(linkType);
	}
}
