/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributedefinition;

import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.client.codelist.common.ItemEditingPanelFactory;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class AttributeDefinitionEditingPanelFactory implements ItemEditingPanelFactory<UIAttributeDefinition, AttributeDefinitionPanel> {
	
	@Inject
	private AttributeDescriptionSuggestOracle attributeDescriptionSuggestOracle;

	
	@Override
	public AttributeDefinitionPanel createPanel(UIAttributeDefinition item) {
		AttributeDefinitionPanel attributeDefinitionPanel = new AttributeDefinitionPanel(item, attributeDescriptionSuggestOracle);
		return attributeDefinitionPanel;
	}

	@Override
	public AttributeDefinitionPanel createPanelForNewItem(UIAttributeDefinition item) {
		AttributeDefinitionPanel attributeDefinitionPanel = new AttributeDefinitionPanel(item, attributeDescriptionSuggestOracle);
		return attributeDefinitionPanel;
	}

}