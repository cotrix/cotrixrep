/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributetype;

import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.client.codelist.common.ItemEditingPanelFactory;

import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class AttributeTypeEditingPanelFactory implements ItemEditingPanelFactory<UIAttributeType, AttributeTypePanel> {

	
	@Override
	public AttributeTypePanel createPanel(UIAttributeType item) {
		AttributeTypePanel attributeTypePanel = new AttributeTypePanel(item);
		return attributeTypePanel;
	}

	@Override
	public AttributeTypePanel createPanelForNewItem(UIAttributeType item) {
		AttributeTypePanel attributeTypePanel = new AttributeTypePanel(item);
		return attributeTypePanel;
	}

}
