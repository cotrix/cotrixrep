/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributedefinition;

import org.cotrix.web.common.client.widgets.dialog.ConfirmDialog;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;
import org.cotrix.web.manage.client.util.LabelHeader;

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
	public ItemPanel<UIAttributeDefinition> createPanel(UIAttributeDefinition item) {
		LabelHeader header = getHeader();
		AttributeDefinitionEditor editor = new AttributeDefinitionEditor(confirmDialog, item, attributeDescriptionSuggestOracle);
		ItemPanel<UIAttributeDefinition> attributeDefinitionPanel = new ItemPanel<UIAttributeDefinition>(editor, header);
		return attributeDefinitionPanel;
	}

	@Override
	public ItemPanel<UIAttributeDefinition> createPanelForNewItem(UIAttributeDefinition item) {
		return createPanel(item);
	}
	
	private LabelHeader getHeader() {
		LabelHeader header = new LabelHeader();
		header.setSwitchVisible(false);

		header.setSaveTitle("Save all changes.");
		header.setRevertTitle("Discard all changes.");
		header.setEditTitle("Make changes.");
		return header;
	}

}
