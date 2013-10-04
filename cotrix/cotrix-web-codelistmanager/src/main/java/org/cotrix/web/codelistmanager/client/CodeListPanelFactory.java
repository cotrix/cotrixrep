/**
 * 
 */
package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelPresenter;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListPanelFactory {
		
	@Inject
	protected EditorEventBusProvider editorEventBusProvider;
	
	@Inject
	protected CodelistIdProvider codelistIdProvider;
	
	@Inject
	protected MetadataEditorProvider metadataEditorProvider;
	
	public CodeListPanelPresenter build(String codelistId)
	{
		codelistIdProvider.setCodelistId(codelistId);
		editorEventBusProvider.generate();
		metadataEditorProvider.generate();
		return CotrixManagerAppGinInjector.INSTANCE.getCodeListPanelPresenter();
	}
}
