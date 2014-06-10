/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributetype;

import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.client.codelist.common.ItemPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypePanel extends ItemPanel<UIAttributeType> {

	public AttributeTypePanel(UIAttributeType attributeType) {
		super(new AttributeTypeEditor(attributeType));
	}
}
