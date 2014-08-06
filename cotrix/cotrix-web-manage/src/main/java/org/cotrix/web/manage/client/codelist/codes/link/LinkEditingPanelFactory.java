/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import static org.cotrix.web.manage.client.codelist.common.Icons.*;
import static org.cotrix.web.manage.client.codelist.common.header.ButtonResourceBuilder.*;

import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;
import org.cotrix.web.manage.client.codelist.common.header.ButtonResources;
import org.cotrix.web.manage.client.codelist.common.header.EditingHeader;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkEditingPanelFactory implements ItemPanelFactory<UILink> {
	
	private static ButtonResources EDIT = create().upFace(icons.redEdit()).hover(icons.redEditHover()).title("Make changes.").build();
	private static ButtonResources REVERT = create().upFace(icons.redCancel()).hover(icons.redCancelHover()).title("Discard all changes.").build();
	private static ButtonResources SAVE = create().upFace(icons.redSave()).hover(icons.redSaveHover()).title("Save all changes.").build();
	private static ButtonResources SWITCH = create().upFace(icons.columnToggle()).hover(icons.columnToggleHover()).downFace(icons.columnToggleDown()).title("To column").build();
	
	@Inject
	private LinksCodelistInfoProvider codelistInfoProvider;

	@Override
	public LinkPanel createPanel(UILink item) {
		EditingHeader header = getHeader();
		LinkPanel linkPanel = new LinkPanel(item, codelistInfoProvider, header);
		return linkPanel;
	}

	@Override
	public LinkPanel createPanelForNewItem(UILink item) {
		EditingHeader header = getHeader();
		LinkPanel linkPanel = new LinkPanel(item, codelistInfoProvider, header);
		return linkPanel;
	}
	
	private EditingHeader getHeader() {
		EditingHeader header = new EditingHeader(icons.link(), EDIT, REVERT, SAVE);
		header.setSwitch(SWITCH);
		header.setSmall();
		return header;
	}
}
