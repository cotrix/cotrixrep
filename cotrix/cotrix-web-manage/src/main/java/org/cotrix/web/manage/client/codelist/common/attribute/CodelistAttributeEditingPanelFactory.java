/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.codelist.common.ItemEditingPanelFactory;
import org.cotrix.web.manage.client.util.Attributes;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistAttributeEditingPanelFactory implements ItemEditingPanelFactory<UIAttribute, AttributePanel> {
	
	@Inject
	private AttributeNameSuggestOracle attributeNameSuggestOracle;

	@Override
	public AttributePanel createPanel(UIAttribute item) {
		AttributePanel attributePanel = new AttributePanel(item, attributeNameSuggestOracle);
		attributePanel.setSwitchVisible(false);
		attributePanel.setReadOnly(Attributes.isSystemAttribute(item));
		return attributePanel;
	}

	@Override
	public AttributePanel createPanelForNewItem(UIAttribute item) {
		AttributePanel attributePanel = new AttributePanel(item, attributeNameSuggestOracle);
		attributePanel.setSwitchVisible(false);
		return attributePanel;
	}

}
