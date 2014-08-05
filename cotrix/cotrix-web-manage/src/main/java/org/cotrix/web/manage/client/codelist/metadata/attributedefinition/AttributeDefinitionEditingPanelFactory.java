/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributedefinition;

import org.cotrix.web.common.client.widgets.dialog.ConfirmDialog;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class AttributeDefinitionEditingPanelFactory implements ItemPanelFactory<UIAttributeDefinition> {
	
	@Inject
	private AttributeDescriptionSuggestOracle attributeDescriptionSuggestOracle;
	
	@Inject
	private ConfirmDialog confirmDialog;
	
	@Override
	public AttributeDefinitionPanel createPanel(UIAttributeDefinition item) {
		AttributeDefinitionPanel attributeDefinitionPanel = new AttributeDefinitionPanel(confirmDialog, item, attributeDescriptionSuggestOracle);
		return attributeDefinitionPanel;
	}

	@Override
	public AttributeDefinitionPanel createPanelForNewItem(UIAttributeDefinition item) {
		AttributeDefinitionPanel attributeDefinitionPanel = new AttributeDefinitionPanel(confirmDialog, item, attributeDescriptionSuggestOracle);
		return attributeDefinitionPanel;
	}

}
