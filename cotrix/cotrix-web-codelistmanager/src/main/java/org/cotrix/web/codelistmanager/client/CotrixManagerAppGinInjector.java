package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.codelist.CodeListAttributesPanel;
import org.cotrix.web.codelistmanager.client.codelist.CodeListEditor;
import org.cotrix.web.codelistmanager.client.codelist.CodeListMetadataPanel;
import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelPresenter;
import org.cotrix.web.codelistmanager.client.codelists.CodeListsPresenter;
import org.cotrix.web.codelistmanager.client.manager.CodeListManagerPresenter;
import org.cotrix.web.share.client.CommonGinModule;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules({CotrixManagerAppGinModule.class, CommonGinModule.class})
public interface CotrixManagerAppGinInjector extends Ginjector {

	public static CotrixManagerAppGinInjector INSTANCE = GWT.create(CotrixManagerAppGinInjector.class);

	public ManagerServiceAsync getRpcService();
	public CotrixManagerAppController getController();
	public CodeListsPresenter getCodeListPresenter();
	public CodeListManagerPresenter getCodeListManagerPresenter();
	
	public CodeListPanelPresenter getCodeListPanelPresenter();
	
	public CodeListEditor getCodeListEditor();
	public CodeListAttributesPanel getCodeListAttributesPanel();
	public CodeListMetadataPanel getCodeListMetadataPanel();
}
