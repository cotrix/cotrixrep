/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.linktype;

import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.codelist.common.ItemPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypePanel extends ItemPanel<UILinkType> {

	public LinkTypePanel(UILinkType type, LinkTypesCodelistInfoProvider codelistInfoProvider) {
		super(new LinkTypeEditor(type, codelistInfoProvider));
	}
}
