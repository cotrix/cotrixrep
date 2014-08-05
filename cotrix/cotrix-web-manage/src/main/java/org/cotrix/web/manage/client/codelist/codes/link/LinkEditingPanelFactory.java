/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.codelist.common.ItemPanelFactory;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkEditingPanelFactory implements ItemPanelFactory<UILink> {
	
	@Inject
	private LinksCodelistInfoProvider codelistInfoProvider;

	@Override
	public LinkPanel createPanel(UILink item) {
		LinkPanel linkPanel = new LinkPanel(item, codelistInfoProvider);
		return linkPanel;
	}

	@Override
	public LinkPanel createPanelForNewItem(UILink item) {
		LinkPanel linkPanel = new LinkPanel(item, codelistInfoProvider);
		return linkPanel;
	}

}
