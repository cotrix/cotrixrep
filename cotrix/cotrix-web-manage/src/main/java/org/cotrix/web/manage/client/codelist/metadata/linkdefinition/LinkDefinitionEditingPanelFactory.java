/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.linkdefinition;

import static org.cotrix.web.common.client.widgets.button.ButtonResourceBuilder.*;
import static org.cotrix.web.manage.client.codelist.common.Icons.*;

import org.cotrix.web.common.client.widgets.button.ButtonResources;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
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
public class LinkDefinitionEditingPanelFactory implements ItemPanelFactory<UILinkDefinition> {
	
	private static ButtonResources EDIT = create().upFace(icons.redEdit()).hover(icons.redEditHover()).title("Make changes.").build();
	private static ButtonResources REVERT = create().upFace(icons.redCancel()).hover(icons.redCancelHover()).title("Discard all changes.").build();
	private static ButtonResources SAVE = create().upFace(icons.redSave()).hover(icons.redSaveHover()).title("Save all changes.").build();


	@Inject
	private LinkDefinitionsCodelistInfoProvider codelistInfoProvider;
	
	@Override
	public ItemPanel<UILinkDefinition> createPanel(UILinkDefinition item) {
		EditingHeader header = getHeader();
		LinkDefinitionDetailsPanel view = new LinkDefinitionDetailsPanel(codelistInfoProvider);
		LinkDefinitionEditor editor = new LinkDefinitionEditor(item, view);
		ItemPanel<UILinkDefinition> linkDefinitionPanel = new ItemPanel<UILinkDefinition>(header, view, editor);
		return linkDefinitionPanel;
	}
	
	private EditingHeader getHeader() {
		EditingHeader header = new EditingHeader(icons.link(), EDIT, REVERT, SAVE);
		return header;
	}

}
