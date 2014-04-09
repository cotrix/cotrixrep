/**
 * 
 */
package org.cotrix.web.manage.client.codelist.linktype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.link.UILinkType;
import org.cotrix.web.manage.client.codelist.attribute.AttributesGridResources;
import org.cotrix.web.manage.client.codelist.linktype.LinkTypePanel.LinkTypePanelListener;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypesPanel extends Composite implements HasEditing {
	
	public interface LinkTypesPanelListener {
		public void onCreate(UILinkType linkType);
		public void onUpdate(UILinkType linkType);
	}
	
	private VerticalPanel panel;
	private List<LinkTypePanel> panels = new ArrayList<LinkTypePanel>();
	
	protected static AttributesGridResources gridResource = GWT.create(AttributesGridResources.class);
	private LinkTypePanel currentSelection;
	
	private Map<String, LinkTypePanel> typeIdToPanel = new HashMap<String, LinkTypePanel>();
	private Map<String, UILinkType> panelIdToType = new HashMap<String, UILinkType>();
	
	@Inject
	private CodelistInfoProvider codelistInfoProvider;
	
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

	public void setListener(LinkTypesPanelListener listener) {
		this.listener = listener;
	}
	public void removeLinkType(UILinkType linkType) {
		LinkTypePanel linkTypePanel = typeIdToPanel.remove(linkType.getId());
		if (linkTypePanel == null) return;
		if (currentSelection == linkTypePanel) currentSelection = null;
		panel.remove(linkTypePanel);
		panels.remove(linkTypePanel);
	}
	
	public void addLinkType(UILinkType linkType) {
		final LinkTypePanel linkTypePanel = new LinkTypePanel(codelistInfoProvider);
		panels.add(linkTypePanel);
		
		typeIdToPanel.put(linkType.getId(), linkTypePanel);
		panelIdToType.put(linkTypePanel.getId(), linkType);
		
		linkTypePanel.setLinkType(linkType);
		linkTypePanel.setListener(new LinkTypePanelListener() {
			
			@Override
			public void onSave(UILinkType linkType) {
				Log.trace("updating type:"+linkType);
				UILinkType oldType = panelIdToType.put(linkTypePanel.getId(), linkType);
				if (oldType!=null) linkType.setId(oldType.getId());
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
		final LinkTypePanel linkTypePanel = new LinkTypePanel(codelistInfoProvider);
		panels.add(linkTypePanel);
		linkTypePanel.setListener(new LinkTypePanelListener() {
			
			@Override
			public void onSave(UILinkType linkType) {
				Log.trace("creating type:"+linkType);
				fireCreate(linkType);
			}
			
			@Override
			public void onCancel() {
				panel.remove(linkTypePanel);
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
		if (currentSelection!=null) return panelIdToType.get(currentSelection.getId());
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
