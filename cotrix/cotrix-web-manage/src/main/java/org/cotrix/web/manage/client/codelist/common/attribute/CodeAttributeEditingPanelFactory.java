/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.codelist.cache.AttributeTypesCache;
import org.cotrix.web.manage.client.codelist.common.ItemEditingPanelFactory;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.util.Attributes;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttributeEditingPanelFactory implements ItemEditingPanelFactory<UIAttribute, AttributePanel> {
	
	@Inject @CurrentCodelist
	private AttributeTypesCache attributeTypesCache;
	
	@Inject
	private AttributeDescriptionSuggestOracle attributeDescriptionSuggestOracle;

	@Override
	public AttributePanel createPanel(UIAttribute item) {
		AttributePanel attributePanel = new AttributePanel(item, attributeDescriptionSuggestOracle, attributeTypesCache);
		attributePanel.setReadOnly(Attributes.isSystemAttribute(item));
		return attributePanel;
	}

	@Override
	public AttributePanel createPanelForNewItem(UIAttribute item) {
		AttributePanel attributePanel = new AttributePanel(item, attributeDescriptionSuggestOracle, attributeTypesCache);
		return attributePanel;
	}

}
