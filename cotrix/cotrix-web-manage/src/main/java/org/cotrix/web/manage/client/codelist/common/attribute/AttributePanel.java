/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.codelist.cache.AttributeDefinitionsCache;
import org.cotrix.web.manage.client.codelist.common.ItemPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributePanel extends ItemPanel<UIAttribute> {

	public AttributePanel(UIAttribute attribute, boolean hasDefinition, AttributeDescriptionSuggestOracle oracle, AttributeDefinitionsCache attributeTypesCache) {
		super(new AttributeEditor(attribute, hasDefinition, oracle, attributeTypesCache));
	}

}
