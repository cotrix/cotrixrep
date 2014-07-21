/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linkdefinition;

import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.shared.modify.GenericCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDefinitionCommand extends GenericCommand<UILinkDefinition> {
	
	public LinkDefinitionCommand() {
	}

	public LinkDefinitionCommand(Action action, UILinkDefinition item) {
		super(action, item);
	}
}
