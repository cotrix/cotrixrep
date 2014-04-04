/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.link.AttributeType;
import org.cotrix.web.common.shared.codelist.link.CodeNameType;
import org.cotrix.web.common.shared.codelist.link.UILinkType;
import org.cotrix.web.common.shared.codelist.link.UILinkType.UIValueType;
import org.cotrix.web.manage.client.codelist.attribute.AttributesGridResources;
import org.cotrix.web.manage.client.codelist.link.LinkTypeDetailsPanel.ValueType;
import org.cotrix.web.manage.client.codelist.link.LinkTypePanel.LinkTypePanelListener;

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
		
		Label header = new Label("Codelist link types");
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
		LinkTypeDetails details = toDetails(linkType);
		final LinkTypePanel linkTypePanel = new LinkTypePanel(codelistInfoProvider);
		panels.add(linkTypePanel);
		
		typeIdToPanel.put(linkType.getId(), linkTypePanel);
		panelIdToType.put(linkTypePanel.getId(), linkType);
		
		linkTypePanel.setLinkDetails(details);
		linkTypePanel.setListener(new LinkTypePanelListener() {
			
			@Override
			public void onSave(LinkTypeDetails details) {
				Log.trace("updating type:"+details);
				UILinkType type = toType(details);
				UILinkType oldType = panelIdToType.put(linkTypePanel.getId(), type);
				if (oldType!=null) type.setId(oldType.getId());
				fireUpdate(type);
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
			public void onSave(LinkTypeDetails details) {
				Log.trace("creating type:"+details);
				UILinkType type = toType(details);
				fireCreate(type);
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
				linkTypePanel.enterEditMode();
			}
		});
	}
	
	private UILinkType toType(LinkTypeDetails details) {
		UIQName name = ValueUtils.getValue(details.getName());
		UICodelist targetCodelist = details.getCodelist();
		String valueFunction = details.getFunction();
		UIValueType valueType = toValueType(details);
		return new UILinkType(null, name, targetCodelist, valueFunction, valueType);
	}
	
	private UIValueType toValueType(LinkTypeDetails details) {
		ValueType type = details.getValueType();
		switch (type) {
			case ATTRIBUTE: return details.getAttribute();
			case NAME: return new CodeNameType();
			case LINK: return null;
			default: throw new IllegalArgumentException("Unknwown value type "+type);
		}
	}
	
	private LinkTypeDetails toDetails(UILinkType linkType) {
		String name = ValueUtils.getLocalPart(linkType.getName());
		UICodelist codelist = linkType.getTargetCodelist();
		ValueType valueType = getValueType(linkType.getValueType());
		AttributeType attribute = valueType==ValueType.ATTRIBUTE?(AttributeType)linkType.getValueType():null;
		String function = null;
		String customFunction = linkType.getValueFunction();
		
		return new LinkTypeDetails(name, codelist, valueType, attribute, function, customFunction);
	}
	
	private ValueType getValueType(UIValueType type) {
		if (type instanceof AttributeType) return ValueType.ATTRIBUTE;
		if (type instanceof CodeNameType) return ValueType.NAME;
		throw new IllegalArgumentException("Unknwown value type "+type);
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
