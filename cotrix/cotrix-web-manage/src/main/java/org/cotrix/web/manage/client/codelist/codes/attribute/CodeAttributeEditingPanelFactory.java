/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.attribute;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.codelist.cache.AttributeDefinitionsCache;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributePanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;
import org.cotrix.web.manage.client.codelist.common.header.ButtonResources;
import org.cotrix.web.manage.client.codelist.common.header.EditingHeader;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.util.Attributes;

import static org.cotrix.web.manage.client.codelist.common.header.ButtonResourceBuilder.create;
import static org.cotrix.web.manage.client.codelist.common.Icons.icons;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttributeEditingPanelFactory implements ItemPanelFactory<UIAttribute> {
	
	private static ButtonResources EDIT = create().upFace(icons.blueEdit()).hover(icons.blueEditHover()).title("Make changes.").build();
	private static ButtonResources REVERT = create().upFace(icons.blueCancel()).hover(icons.blueCancelHover()).title("Discard all changes.").build();
	private static ButtonResources SAVE = create().upFace(icons.blueSave()).hover(icons.blueSaveHover()).title("Save all changes.").build();
	private static ButtonResources SWITCH = create().upFace(icons.columnToggle()).hover(icons.columnToggleHover()).downFace(icons.columnToggleDown()).title("To column").build();
	
	@Inject @CurrentCodelist
	private AttributeDefinitionsCache attributeTypesCache;
	
	@Inject
	private AttributeDescriptionSuggestOracle attributeDescriptionSuggestOracle;

	@Override
	public AttributePanel createPanel(UIAttribute item) {
		EditingHeader header = getHeader(item);
		AttributePanel attributePanel = new AttributePanel(item, true, attributeDescriptionSuggestOracle, attributeTypesCache, header);
		attributePanel.setReadOnly(Attributes.isSystemAttribute(item));
		return attributePanel;
	}

	@Override
	public AttributePanel createPanelForNewItem(UIAttribute item) {
		EditingHeader header = getHeader(item);
		AttributePanel attributePanel = new AttributePanel(item, true, attributeDescriptionSuggestOracle, attributeTypesCache, header);
		return attributePanel;
	}
	
	private EditingHeader getHeader(UIAttribute item) {
		EditingHeader header = new EditingHeader(Attributes.isSystemAttribute(item)?icons.attributeDisabled():icons.attribute(), EDIT, REVERT, SAVE);
		header.setSwitch(SWITCH);
		header.setSmall();
		return header;
	}

}
