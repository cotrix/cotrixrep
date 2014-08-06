/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attribute;

import static org.cotrix.web.manage.client.codelist.common.Icons.*;
import static org.cotrix.web.manage.client.codelist.common.header.ButtonResourceBuilder.*;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.codelist.cache.AttributeDefinitionsCache;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeEditor;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;
import org.cotrix.web.manage.client.codelist.common.header.ButtonResources;
import org.cotrix.web.manage.client.codelist.common.header.EditingHeader;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.util.Attributes;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistAttributeEditingPanelFactory implements ItemPanelFactory<UIAttribute> {
	
	private static ButtonResources EDIT = create().upFace(icons.blueEdit()).hover(icons.blueEditHover()).title("Make changes.").build();
	private static ButtonResources REVERT = create().upFace(icons.blueCancel()).hover(icons.blueCancelHover()).title("Discard all changes.").build();
	private static ButtonResources SAVE = create().upFace(icons.blueSave()).hover(icons.blueSaveHover()).title("Save all changes.").build();

	
	@Inject
	private AttributeDescriptionSuggestOracle attributeDescriptionSuggestOracle;
	
	@Inject @CurrentCodelist
	private AttributeDefinitionsCache attributeTypesCache;

	@Override
	public ItemPanel<UIAttribute> createPanel(UIAttribute item) {
		EditingHeader header = getHeader(item);
		AttributeEditor editor = new AttributeEditor(item, false, attributeDescriptionSuggestOracle, attributeTypesCache);
	
		ItemPanel<UIAttribute> attributePanel = new ItemPanel<UIAttribute>(editor, header);
		attributePanel.setReadOnly(Attributes.isSystemAttribute(item));
		return attributePanel;
	}
	
	private EditingHeader getHeader(UIAttribute item) {
		EditingHeader header = new EditingHeader(Attributes.isSystemAttribute(item)?icons.attributeDisabled():icons.attribute(), EDIT, REVERT, SAVE);
		header.setSmall();
		return header;
	}

}
