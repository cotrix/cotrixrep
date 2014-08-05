/**
 * 
 */
package org.cotrix.web.manage.client.codelist.codes.link;

import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkPanel extends ItemPanel<UILink> {

	public LinkPanel(UILink link, LinksCodelistInfoProvider codelistInfoProvider) {
		super(new LinkEditor(link, codelistInfoProvider));
	}

}
