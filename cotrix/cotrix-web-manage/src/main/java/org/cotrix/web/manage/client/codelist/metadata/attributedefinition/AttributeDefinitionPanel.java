/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributedefinition;

import org.cotrix.web.common.client.widgets.dialog.ConfirmDialog;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelHeader;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitionPanel extends ItemPanel<UIAttributeDefinition> {

	public AttributeDefinitionPanel(ConfirmDialog confirmDialog, UIAttributeDefinition attributeDefinition, AttributeDescriptionSuggestOracle attributeDescriptionSuggestOracle, ItemPanelHeader header) {
		super(new AttributeDefinitionEditor(confirmDialog, attributeDefinition, attributeDescriptionSuggestOracle), header);
	}
}
