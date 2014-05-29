/**
 * 
 */
package org.cotrix.web.manage.shared.modify.attribute;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.shared.modify.GenericCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeCommand  extends GenericCommand<UIAttribute> {
	
	public AttributeCommand() {
	}

	public AttributeCommand(Action action, UIAttribute item) {
		super(action, item);
	}

}
