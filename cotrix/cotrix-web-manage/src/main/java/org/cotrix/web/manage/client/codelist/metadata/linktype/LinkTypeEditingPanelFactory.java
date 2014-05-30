/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.linktype;

import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.codelist.common.ItemEditingPanelFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class LinkTypeEditingPanelFactory implements	ItemEditingPanelFactory<UILinkType, LinkTypePanel> {

	@Inject
	private LinkTypesCodelistInfoProvider codelistInfoProvider;
	
	@Override
	public LinkTypePanel createPanel(UILinkType item) {
		LinkTypePanel linkTypePanel = new LinkTypePanel(item, codelistInfoProvider);
		return linkTypePanel;
	}

	@Override
	public LinkTypePanel createPanelForNewItem(UILinkType item) {
		LinkTypePanel linkTypePanel = new LinkTypePanel(item, codelistInfoProvider);
		return linkTypePanel;
	}

}
