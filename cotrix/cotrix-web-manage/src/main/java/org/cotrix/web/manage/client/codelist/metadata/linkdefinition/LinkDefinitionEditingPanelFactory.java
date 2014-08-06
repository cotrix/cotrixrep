/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.linkdefinition;

import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanelFactory;
import org.cotrix.web.manage.client.util.LabelHeader;

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
		LabelHeader header = getHeader();
		LinkDefinitionPanel linkDefinitionPanel = new LinkDefinitionPanel(item, codelistInfoProvider, header);
		return linkDefinitionPanel;
	}

	@Override
	public LinkDefinitionPanel createPanelForNewItem(UILinkDefinition item) {
		LabelHeader header = getHeader();
		LinkDefinitionPanel linkDefinitionPanel = new LinkDefinitionPanel(item, codelistInfoProvider, header);
		return linkDefinitionPanel;
	}
	
	
	private LabelHeader getHeader() {
		LabelHeader header = new LabelHeader();
		header.setSwitchVisible(false);

		header.setSaveTitle("Save all changes.");
		header.setRevertTitle("Discard all changes.");
		header.setEditTitle("Make changes.");
		return header;
	}

}
