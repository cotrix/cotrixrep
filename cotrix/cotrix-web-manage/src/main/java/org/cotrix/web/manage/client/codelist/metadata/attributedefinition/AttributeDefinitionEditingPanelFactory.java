/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributedefinition;

import static org.cotrix.web.common.client.widgets.button.ButtonResourceBuilder.*;
import static org.cotrix.web.manage.client.codelist.common.Icons.*;

import org.cotrix.web.common.client.widgets.button.ButtonResources;
import org.cotrix.web.common.client.widgets.dialog.ConfirmDialog;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;
import org.cotrix.web.manage.client.codelist.common.form.EditingHeader;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class AttributeDefinitionEditingPanelFactory implements ItemPanelFactory<UIAttributeDefinition> {
	
	private static ButtonResources EDIT = create().upFace(icons.blueEdit()).hover(icons.blueEditHover()).title("Make changes.").build();
	private static ButtonResources REVERT = create().upFace(icons.blueCancel()).hover(icons.blueCancelHover()).title("Discard all changes.").build();
	private static ButtonResources SAVE = create().upFace(icons.blueSave()).hover(icons.blueSaveHover()).title("Save all changes.").build();
	
	@Inject
	private AttributeDescriptionSuggestOracle attributeDescriptionSuggestOracle;
	
	@Inject
	private ConfirmDialog confirmDialog;
	
	@Override
	public ItemPanel<UIAttributeDefinition> createPanel(UIAttributeDefinition item) {
		EditingHeader header = buildHeader();
		AttributeDefinitionDetailsPanel view = new AttributeDefinitionDetailsPanel(attributeDescriptionSuggestOracle);
		
		AttributeDefinitionEditor editor = new AttributeDefinitionEditor(confirmDialog, item, view);
		
		ItemPanel<UIAttributeDefinition> attributeDefinitionPanel = new ItemPanel<UIAttributeDefinition>(header, view, editor);
		return attributeDefinitionPanel;
	}
	
	private EditingHeader buildHeader() {
		EditingHeader header = new EditingHeader(icons.attribute(), EDIT, REVERT, SAVE);
		return header;
	}

}
