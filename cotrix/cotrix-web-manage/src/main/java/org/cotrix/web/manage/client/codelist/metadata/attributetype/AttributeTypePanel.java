/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributetype;

import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.client.codelist.common.ItemPanel;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypePanel extends ItemPanel<UIAttributeType> {

	public AttributeTypePanel(UIAttributeType attributeType, AttributeDescriptionSuggestOracle attributeDescriptionSuggestOracle) {
		super(new AttributeTypeEditor(attributeType, attributeDescriptionSuggestOracle));
	}
}
