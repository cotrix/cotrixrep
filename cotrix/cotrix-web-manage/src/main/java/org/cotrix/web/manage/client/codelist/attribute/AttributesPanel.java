/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attribute;

import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemsEditingListener.SwitchState;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.binder.EventBinder;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributesPanel extends Composite implements HasEditing, HasSelectionChangedHandlers {

	public interface AttributesPanelListener extends ItemsEditingListener<UIAttribute> {
	}

	interface AttributesPanelEventBinder extends EventBinder<AttributesPanel> {}

	private ItemsEditingPanel<UIAttribute, AttributePanel> editingPanel;
	
	@Inject
	private CotrixManagerResources resources;
	
	public AttributesPanel() {
		editingPanel = new ItemsEditingPanel<UIAttribute, AttributePanel>("Attributes", "no attributes");
		initWidget(editingPanel);
	}
	
	public void setSelectedCode(String code) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendHtmlConstant("<span>Attributes</span>");
		if (code!=null) {
			sb.appendHtmlConstant("&nbsp;for&nbsp;<span class=\""+resources.css().headerCode()+"\">");
			sb.append(SafeHtmlUtils.fromString(code));
			sb.appendHtmlConstant("</span>");
		}
		
		editingPanel.setHeaderText(sb.toSafeHtml());
	}
	
	public void syncWithModel(UIAttribute attribute) {
		editingPanel.synchWithModel(attribute);
	}

	public void setListener(AttributesPanelListener listener) {
		editingPanel.setListener(listener);
	}

	public void removeAttribute(UIAttribute attribute) {
		editingPanel.removeItem(attribute);
	}

	public void clear() {
		editingPanel.clear();
	}
	
	public void setSwitchState(UIAttribute item, SwitchState state) {
		editingPanel.setSwitchState(item, state);
	}

	public void addAttribute(UIAttribute attribute) {
		final AttributePanel attributePanel = new AttributePanel(attribute);
		editingPanel.addItemPanel(attributePanel, attribute);
	}

	public void addNewAttribute() {
		UIAttribute attribute = new UIAttribute();
		AttributePanel attributePanel = new AttributePanel(attribute);
		editingPanel.addNewItemPanel(attributePanel, attribute);
	}

	public UIAttribute getSelectedAttribute() {
		return editingPanel.getSelectedItem();
	}

	@Override
	public void setEditable(boolean editable) {
		editingPanel.setEditable(editable);
	}

	@Override
	public HandlerRegistration addSelectionChangeHandler(Handler handler) {
		return editingPanel.addSelectionChangeHandler(handler);
	}
}
