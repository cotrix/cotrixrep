/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attribute;

import static org.cotrix.web.common.client.widgets.button.ButtonResourceBuilder.*;
import static org.cotrix.web.manage.client.codelist.common.Icons.*;

import org.cotrix.web.common.client.widgets.button.ButtonResources;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.codelist.cache.AttributeDefinitionsCache;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDetailsPanel;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeEditor;
import org.cotrix.web.manage.client.codelist.common.form.EditingHeader;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;
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
		
		AttributeDetailsPanel view = new AttributeDetailsPanel(attributeDescriptionSuggestOracle, attributeTypesCache);
		view.setDefinitionVisible(false);
		
		AttributeEditor editor = new AttributeEditor(item, view);
	
		ItemPanel<UIAttribute> attributePanel = new ItemPanel<UIAttribute>(header, view, editor);
		attributePanel.setReadOnly(Attributes.isSystemAttribute(item));
		return attributePanel;
	}
	
	private EditingHeader getHeader(UIAttribute item) {
		boolean systemAttribute = Attributes.isSystemAttribute(item);
		EditingHeader header = new EditingHeader(systemAttribute?icons.attributeDisabled():icons.attribute(), EDIT, REVERT, SAVE);
		header.setTitleGreyed(systemAttribute);
		header.setSmall();
		return header;
	}

}
