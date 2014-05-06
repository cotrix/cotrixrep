/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.HasValue;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.codelist.attribute.event.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener.SwitchState;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinksPanel extends Composite implements HasEditing {

	public interface LinksPanelListener extends ItemsEditingListener<UILink> {
	}

	interface LinksPanelEventBinder extends EventBinder<LinksPanel> {}

	@Inject
	private LinksCodelistInfoProvider codelistInfoProvider;

	private ItemsEditingPanel<UILink, LinkPanel> editingPanel;
	
	@Inject
	private CotrixManagerResources resources;
	
	public LinksPanel() {
		editingPanel = new ItemsEditingPanel<UILink, LinkPanel>("Links", "no links");
		initWidget(editingPanel);
	}
	
	public void setSelectedCode(String code) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendHtmlConstant("<span>Links</span>");
		if (code!=null) {
			sb.appendHtmlConstant("&nbsp;for&nbsp;<span class=\""+resources.css().headerCode()+"\">");
			sb.append(SafeHtmlUtils.fromString(code));
			sb.appendHtmlConstant("</span>");
		}
		
		editingPanel.setHeaderText(sb.toSafeHtml());
	}

	@Inject
	void bind(LinksPanelEventBinder binder, @EditorBus EventBus editorBus) {
		binder.bindEventHandlers(this, editorBus);
	}

	@EventHandler
	void onAttributesUpdated(AttributesUpdatedEvent event) {
		HasAttributes attributedItem = event.getAttributedItem();
		Log.trace("attributes updated for "+attributedItem);
		if (attributedItem instanceof UILink) {
			UILink link = (UILink) attributedItem;
			editingPanel.synchWithModel(link);
			//model already updated on save manager
		}
	}

	@EventHandler
	void onValueUpdated(ValueUpdatedEvent event) {
		HasValue valuedItem = event.getHasValue();
		Log.trace("value updated for "+valuedItem);
		if (valuedItem instanceof UILink) {
			UILink link = (UILink) valuedItem;
			editingPanel.synchWithModel(link);
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
		editingPanel.setListener(listener);
	}

	public void removeLink(UILink link) {
		editingPanel.removeItem(link);
	}

	public void clear() {
		editingPanel.clear();
	}
	
	public void setSwitchState(UILink item, SwitchState state) {
		editingPanel.setSwitchState(item, state);
	}

	public void addLink(UILink link) {
		final LinkPanel linkPanel = new LinkPanel(link, codelistInfoProvider);
		editingPanel.addItemPanel(linkPanel, link);
	}

	public void addNewLink() {
		UILink link = new UILink();
		LinkPanel linkPanel = new LinkPanel(link, codelistInfoProvider);
		editingPanel.addNewItemPanel(linkPanel, link);
	}

	public UILink getSelectedType() {
		return editingPanel.getSelectedItem();
	}

	@Override
	public void setEditable(boolean editable) {
		editingPanel.setEditable(editable);
	}
}
