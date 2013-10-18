/**
 * 
 */
package org.cotrix.web.codelistmanager.client.di;

import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinInjector;
import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelPresenter;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistPanelFactory {
		
	@Inject
	protected EditorEventBusProvider editorEventBusProvider;
	
	@Inject
	protected CodelistIdProvider codelistIdProvider;
	
	@Inject
	protected MetadataEditorProvider metadataEditorProvider;
	
	@Inject
	protected MetadataAttributeEditorProvider metadataAttributeEditorProvider;
	
	@Inject
	protected CodeEditorProvider codeListRowEditorProvider;
	
	@Inject
	protected CodeAttributeEditorProvider codeAttributeEditorProvider;
	
	
	public CodelistPanelPresenter build(String codelistId)
	{
		codelistIdProvider.setCodelistId(codelistId);
		editorEventBusProvider.generate();
		metadataEditorProvider.generate();
		codeListRowEditorProvider.generate();
		codeAttributeEditorProvider.generate();
		metadataAttributeEditorProvider.generate();
		
		return CotrixManagerAppGinInjector.INSTANCE.getCodeListPanelPresenter();
	}
}
