/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.codelist.attribute.AttributesGridResources;
import org.cotrix.web.manage.client.codelist.attribute.event.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.codelist.link.LinkPanel.LinkPanelListener;
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
public class LinksPanel extends Composite implements HasEditing {
	
	public interface LinksPanelListener {
		public void onCreate(UILink link);
		public void onUpdate(UILink link);
	}
	
	interface LinksPanelEventBinder extends EventBinder<LinksPanel> {}
	
	private VerticalPanel panel;
	private List<LinkPanel> panels = new ArrayList<LinkPanel>();
	
	protected static AttributesGridResources gridResource = GWT.create(AttributesGridResources.class);
	private LinkPanel currentSelection;
	
	private Map<String, LinkPanel> typeIdToPanel = new HashMap<String, LinkPanel>();
	private Map<String, UILink> panelIdToLink = new HashMap<String, UILink>();
	
	@Inject
	private LinksCodelistInfoProvider codelistInfoProvider;
	
	private LinksPanelListener listener;
	
	public LinksPanel() {
		panel = new VerticalPanel();
		panel.setWidth("100%");
		
		gridResource.dataGridStyle().ensureInjected();
		
		Label header = new Label("Links");
		header.setStyleName(gridResource.dataGridStyle().dataGridHeader());
		panel.add(header);
		
		initWidget(panel);
	}
	
	@Inject
	protected void bind(LinksPanelEventBinder binder, @EditorBus EventBus editorBus) {
		binder.bindEventHandlers(this, editorBus);
	}
	
	@EventHandler
	void onCodeSelected(AttributesUpdatedEvent event) {
		HasAttributes attributedItem = event.getAttributedItem();
		if (attributedItem instanceof UILink) {
			UILink link = (UILink) attributedItem;
			LinkPanel panel = typeIdToPanel.get(link.getId());
			panel.setLink(link);
			//model already updated on save manager
		}
	}

	/**
	 * Used for tests
	 * @param codelistInfoProvider
	 */
	public void setCodelistInfoProvider(LinksCodelistInfoProvider codelistInfoProvider) {
		this.codelistInfoProvider = codelistInfoProvider;
	}

	public void setListener(LinksPanelListener listener) {
		this.listener = listener;
	}
	public void removeLink(UILink link) {
		LinkPanel linkTypePanel = typeIdToPanel.remove(link.getId());
		if (linkTypePanel == null) return;
		if (currentSelection == linkTypePanel) currentSelection = null;
		panel.remove(linkTypePanel);
		panels.remove(linkTypePanel);
	}
	
	public void addLink(UILink link) {
		final LinkPanel linkPanel = new LinkPanel(codelistInfoProvider);
		panels.add(linkPanel);
		
		typeIdToPanel.put(link.getId(), linkPanel);
		panelIdToLink.put(linkPanel.getId(), link);
		
		linkPanel.setLink(link);
		linkPanel.setListener(new LinkPanelListener() {
			
			@Override
			public void onSave(UILink link) {
				Log.trace("updating type:"+link);
				UILink oldLink = panelIdToLink.put(linkPanel.getId(), link);
				if (oldLink!=null) link.setId(oldLink.getId());
				fireUpdate(link);
			}
			
			@Override
			public void onCancel() {
				
			}

			@Override
			public void onSelect() {
				updateSelection(linkPanel);				
			}
		});
		panel.add(linkPanel);
	}
	
	public void addNewLink() {
		final LinkPanel linkPanel = new LinkPanel(codelistInfoProvider);
		panels.add(linkPanel);
		linkPanel.setListener(new LinkPanelListener() {
			
			@Override
			public void onSave(UILink link) {
				Log.trace("creating link:"+link);
				fireCreate(link);
			}
			
			@Override
			public void onCancel() {
				panel.remove(linkPanel);
			}

			@Override
			public void onSelect() {
				updateSelection(linkPanel);
			}
		});
		panel.add(linkPanel);
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				linkPanel.enterEditMode(true);
			}
		});
	}
	
	private void updateSelection(LinkPanel selected) {
		Log.trace("selected "+selected);
		if (currentSelection!=null) currentSelection.setSelected(false);
		selected.setSelected(true);
		currentSelection = selected;
	}
	
	public UILink getSelectedType() {
		if (currentSelection!=null) return panelIdToLink.get(currentSelection.getId());
		return null;
	}

	@Override
	public void setEditable(boolean editable) {
		for (LinkPanel linkTypePanel:panels) linkTypePanel.setEditable(editable);
	}

	private void fireUpdate(UILink link) {
		if (listener!=null) listener.onUpdate(link);
	}
	
	private void fireCreate(UILink link) {
		if (listener!=null) listener.onCreate(link);
	}
}
