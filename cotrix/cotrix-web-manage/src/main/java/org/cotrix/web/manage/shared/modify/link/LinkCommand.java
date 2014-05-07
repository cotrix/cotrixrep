/**
 * 
 */
package org.cotrix.web.manage.shared.modify.link;

import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.shared.modify.ContainsAttributed;
import org.cotrix.web.manage.shared.modify.GenericCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkCommand extends GenericCommand<UILink> implements ContainsAttributed {

	public LinkCommand() {
	}

	public LinkCommand(Action action, UILink item) {
		super(action, item);
	}

	@Override
	public HasAttributes getAttributed() {
		return getItem();
	}
}
