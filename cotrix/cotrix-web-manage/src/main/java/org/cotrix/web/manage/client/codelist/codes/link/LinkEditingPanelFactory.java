/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import static org.cotrix.web.common.client.widgets.button.ButtonResourceBuilder.*;
import static org.cotrix.web.manage.client.codelist.common.Icons.*;

import org.cotrix.web.common.client.widgets.button.ButtonResources;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.codelist.common.form.EditingHeader;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkEditingPanelFactory implements ItemPanelFactory<UILink> {
	

	private static ButtonResources REVERT = create().upFace(icons.redCancel()).hover(icons.redCancelHover()).title("Discard all changes.").build();
	private static ButtonResources SAVE = create().upFace(icons.redSave()).hover(icons.redSaveHover()).title("Save all changes.").build();
	private static ButtonResources SWITCH = create().upFace(icons.columnToggle()).hover(icons.columnToggleHover()).downFace(icons.columnToggleDown()).title("To column").build();
	
	@Inject
	private LinksCodelistInfoProvider codelistInfoProvider;

	@Override
	public ItemPanel<UILink> createPanel(UILink item) {
		EditingHeader header = getHeader();
		LinkDetailsPanel view = new LinkDetailsPanel(codelistInfoProvider);
		LinkEditor editor = new LinkEditor(item, view);
		ItemPanel<UILink> linkPanel = new ItemPanel<UILink>(header, view, editor);
		return linkPanel;
	}
	
	private EditingHeader getHeader() {
		EditingHeader header = new EditingHeader(icons.link(), RED_EDIT, REVERT, SAVE);
		header.setSwitch(SWITCH);
		header.setSmall();
		return header;
	}
}
