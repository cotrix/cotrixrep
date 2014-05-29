/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linktype;

import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.shared.modify.GenericCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypeCommand extends GenericCommand<UILinkType> {
	
	public LinkTypeCommand() {
	}

	public LinkTypeCommand(Action action, UILinkType item) {
		super(action, item);
	}
}
