/**
 * 
 */
package org.cotrix.web.manage.shared.modify.attributedefinition;

import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.shared.modify.GenericCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitionCommand extends GenericCommand<UIAttributeDefinition> {

	public AttributeDefinitionCommand() {
	}

	public AttributeDefinitionCommand(Action action, UIAttributeDefinition item) {
		super(action, item);
	}
}
