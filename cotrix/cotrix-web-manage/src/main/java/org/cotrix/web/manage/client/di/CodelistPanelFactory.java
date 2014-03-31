/**
 * 
 */
package org.cotrix.web.manage.client.di;

import org.cotrix.web.common.shared.codelist.UICodelist;
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
	private EditorEventBusProvider editorEventBusProvider;
	
	@Inject
	private CodelistIdProvider codelistIdProvider;
	
	@Inject
	private CodelistProvider codelistProvider;
	
	
	public CodelistPanelPresenter build(UICodelist codelist)
	{
		codelistIdProvider.setCodelistId(codelist.getId());
		codelistProvider.setCodelist(codelist);
		editorEventBusProvider.generate();
		
		return CotrixManageGinInjector.INSTANCE.getCodeListPanelPresenter();
	}
}
