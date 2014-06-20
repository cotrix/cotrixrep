/**
 * 
 */
package org.cotrix.web.manage.client.di;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.CotrixManageGinInjector;
import org.cotrix.web.manage.client.codelist.CodelistPanelController;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistPanelFactory {
		
	@Inject
	private CodelistBusProvider editorEventBusProvider;
	
	@Inject
	private CodelistIdProvider codelistIdProvider;
	
	@Inject
	private CodelistProvider codelistProvider;
	
	@Inject
	private LinkTypesCacheProvider linkTypesCacheProvider;
	
	@Inject
	private AttributeTypesCacheProvider attributeTypesCacheProvider;
	
	
	public CodelistPanelController build(UICodelist codelist)
	{
		codelistIdProvider.setCodelistId(codelist.getId());
		codelistProvider.setCodelist(codelist);
		editorEventBusProvider.generate();
		
		linkTypesCacheProvider.generate();
		attributeTypesCacheProvider.generate();
		
		return CotrixManageGinInjector.INSTANCE.getCodeListPanelPresenter();
	}
}
