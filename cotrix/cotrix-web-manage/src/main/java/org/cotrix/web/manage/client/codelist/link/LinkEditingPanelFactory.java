/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.codelist.common.ItemEditingPanelFactory;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkEditingPanelFactory implements ItemEditingPanelFactory<UILink, LinkPanel> {
	
	
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
