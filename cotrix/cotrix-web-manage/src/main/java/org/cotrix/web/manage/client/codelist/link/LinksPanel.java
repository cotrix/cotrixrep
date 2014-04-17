/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.cotrix.web.common.client.util.InstanceMap;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.HasValue;
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

	private VerticalPanel mainPanel;
	private List<LinkPanel> panels = new ArrayList<LinkPanel>();

	protected static AttributesGridResources gridResource = GWT.create(AttributesGridResources.class);
	private LinkPanel currentSelection;

	private InstanceMap<UILink, LinkPanel> instances = new InstanceMap<UILink, LinkPanel>();

	@Inject
	private LinksCodelistInfoProvider codelistInfoProvider;

	private LinksPanelListener listener;

	private boolean editable;

	public LinksPanel() {
		mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");

		gridResource.dataGridStyle().ensureInjected();

		Label header = new Label("Links");
		header.setStyleName(gridResource.dataGridStyle().dataGridHeader());
		mainPanel.add(header);

		initWidget(mainPanel);

		editable = false;
	}

	@Inject
	protected void bind(LinksPanelEventBinder binder, @EditorBus EventBus editorBus) {
		binder.bindEventHandlers(this, editorBus);
	}

	@EventHandler
	void onAttributesUpdated(AttributesUpdatedEvent event) {
		HasAttributes attributedItem = event.getAttributedItem();
		if (attributedItem instanceof UILink) {
			UILink link = (UILink) attributedItem;
			LinkPanel panel = instances.get(link);
			panel.syncWithModel();
			//model already updated on save manager
		}
	}

	@EventHandler
	void onValueUpdated(ValueUpdatedEvent event) {
		HasValue valuedItem = event.getHasValue();
		if (valuedItem instanceof UILink) {
			Log.trace("value updated for "+valuedItem);
			UILink link = (UILink) valuedItem;
			LinkPanel panel = instances.get(link);
			panel.syncWithModel();
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
		LinkPanel linkPanel = instances.remove(link);
		if (linkPanel == null) return;
		if (currentSelection == linkPanel) currentSelection = null;
		mainPanel.remove(linkPanel);
		panels.remove(linkPanel);
	}

	public void clear() {
		for (LinkPanel panel:panels) mainPanel.remove(panel);
		instances.clear();
	}

	public void addLink(UILink link) {
		final LinkPanel linkPanel = new LinkPanel(link, codelistInfoProvider);
		linkPanel.setEditable(editable);
		panels.add(linkPanel);

		instances.put(link, linkPanel);

		linkPanel.setListener(new LinkPanelListener() {

			@Override
			public void onSave(UILink link) {
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
		mainPanel.add(linkPanel);
	}

	public void addNewLink() {
		UILink link = new UILink();
		
		final LinkPanel linkPanel = new LinkPanel(link, codelistInfoProvider);
		instances.put(link, linkPanel);
		
		panels.add(linkPanel);
		linkPanel.setListener(new LinkPanelListener() {

			boolean created = false;

			@Override
			public void onSave(UILink link) {
				if (!created) {
					fireCreate(link);
					created = true;
				} else fireUpdate(link);
			}

			@Override
			public void onCancel() {
				if (!created) mainPanel.remove(linkPanel);
			}

			@Override
			public void onSelect() {
				updateSelection(linkPanel);
			}
		});
		mainPanel.add(linkPanel);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				linkPanel.enterEditMode();
			}
		});
	}

	private void updateSelection(LinkPanel selected) {
		if (currentSelection!=null) currentSelection.setSelected(false);
		selected.setSelected(true);
		currentSelection = selected;
	}

	public UILink getSelectedType() {
		if (currentSelection!=null) return instances.getByValue(currentSelection);
		return null;
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
		for (LinkPanel linkTypePanel:panels) linkTypePanel.setEditable(editable);
	}

	private void fireUpdate(UILink link) {
		if (listener!=null) listener.onUpdate(link);
	}

	private void fireCreate(UILink link) {
		if (listener!=null) listener.onCreate(link);
	}
}
