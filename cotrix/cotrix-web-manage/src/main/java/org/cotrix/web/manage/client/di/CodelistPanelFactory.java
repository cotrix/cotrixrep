/**
 * 
 */
package org.cotrix.web.manage.client.di;

import org.cotrix.web.manage.client.CotrixManageGinInjector;
import org.cotrix.web.manage.client.codelist.CodelistPanelPresenter;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistPanelFactory {
		
	@Inject
	protected EditorEventBusProvider editorEventBusProvider;
	
	@Inject
	protected CodelistIdProvider codelistIdProvider;
	
	
	public CodelistPanelPresenter build(String codelistId)
	{
		codelistIdProvider.setCodelistId(codelistId);
		editorEventBusProvider.generate();
		
		return CotrixManageGinInjector.INSTANCE.getCodeListPanelPresenter();
	}
}
