/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.linkdefinition;

import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.client.codelist.common.ItemPanelFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class LinkDefinitionEditingPanelFactory implements ItemPanelFactory<UILinkDefinition> {

	@Inject
	private LinkDefinitionsCodelistInfoProvider codelistInfoProvider;
	
	@Override
	public LinkDefinitionPanel createPanel(UILinkDefinition item) {
		LinkDefinitionPanel linkDefinitionPanel = new LinkDefinitionPanel(item, codelistInfoProvider);
		return linkDefinitionPanel;
	}

	@Override
	public LinkDefinitionPanel createPanelForNewItem(UILinkDefinition item) {
		LinkDefinitionPanel linkDefinitionPanel = new LinkDefinitionPanel(item, codelistInfoProvider);
		return linkDefinitionPanel;
	}

}
