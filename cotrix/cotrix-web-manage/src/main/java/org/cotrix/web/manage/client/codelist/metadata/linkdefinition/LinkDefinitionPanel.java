/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.linkdefinition;

import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.client.codelist.common.ItemPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDefinitionPanel extends ItemPanel<UILinkDefinition> {

	public LinkDefinitionPanel(UILinkDefinition type, LinkDefinitionsCodelistInfoProvider codelistInfoProvider) {
		super(new LinkDefinitionEditor(type, codelistInfoProvider));
	}
}
