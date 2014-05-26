/**
 * 
 */
package org.cotrix.web.manage.shared.modify.attributetype;

import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.shared.modify.GenericCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypeCommand extends GenericCommand<UIAttributeType> {

	public AttributeTypeCommand() {
	}

	public AttributeTypeCommand(Action action, UIAttributeType item) {
		super(action, item);
	}
}
