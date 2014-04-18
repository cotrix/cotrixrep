/**
 * 
 */
package org.cotrix.web.manage.client.codelist.linktype;

import javax.inject.Inject;

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.codelist.attribute.event.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.event.EditorBus;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypesPanel extends Composite implements HasEditing {
	
	public interface LinkTypesPanelListener extends ItemsEditingListener<UILinkType> {
		public void onCreate(UILinkType linkType);
		public void onUpdate(UILinkType linkType);
	}
	
	interface LinkTypesPanelEventBinder extends EventBinder<LinkTypesPanel> {}
	
	@Inject
	private LinkTypesCodelistInfoProvider codelistInfoProvider;
	
	private ItemsEditingPanel<UILinkType, LinkTypePanel> editingPanel;
	
	public LinkTypesPanel() {
		editingPanel = new ItemsEditingPanel<UILinkType, LinkTypePanel>("Codelist Links");
		initWidget(editingPanel);
	}
	
	@Inject
	void bind(LinkTypesPanelEventBinder binder, @EditorBus EventBus editorBus) {
		binder.bindEventHandlers(this, editorBus);
	}
	
	@EventHandler
	void onAttributesUpdated(AttributesUpdatedEvent event) {
		HasAttributes attributedItem = event.getAttributedItem();
		if (attributedItem instanceof UILinkType) {
			Log.trace("updated attribues "+attributedItem);
			UILinkType linkType = (UILinkType) attributedItem;
			editingPanel.synchWithModel(linkType);
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
		editingPanel.setListener(listener);
	}
	
	public void removeLinkType(UILinkType linkType) {
		editingPanel.removeItem(linkType);
	}
	
	public void addLinkType(UILinkType linkType) {
		final LinkTypePanel linkTypePanel = new LinkTypePanel(linkType, codelistInfoProvider);
		editingPanel.addItemPanel(linkTypePanel, linkType);
	}
	
	public void addNewLinkType() {
		UILinkType linkType = new UILinkType();
		final LinkTypePanel linkTypePanel = new LinkTypePanel(linkType, codelistInfoProvider);
		editingPanel.addNewItemPanel(linkTypePanel, linkType);
	}
	
	public UILinkType getSelectedType() {
		return editingPanel.getSelectedItem();
	}

	@Override
	public void setEditable(boolean editable) {
		editingPanel.setEditable(editable);
	}
}
