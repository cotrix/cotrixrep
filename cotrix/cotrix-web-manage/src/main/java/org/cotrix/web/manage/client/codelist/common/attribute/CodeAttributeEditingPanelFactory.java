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
	
	@Inject
	private AttributeNameSuggestOracle attributeNameSuggestOracle;
	
	@Inject @CurrentCodelist
	private AttributeTypesCache attributeTypesCache;

	@Override
	public AttributePanel createPanel(UIAttribute item) {
		AttributePanel attributePanel = new AttributePanel(item, attributeNameSuggestOracle, attributeTypesCache);
		attributePanel.setReadOnly(Attributes.isSystemAttribute(item));
		return attributePanel;
	}

	@Override
	public AttributePanel createPanelForNewItem(UIAttribute item) {
		AttributePanel attributePanel = new AttributePanel(item, attributeNameSuggestOracle, attributeTypesCache);
		return attributePanel;
	}

}
