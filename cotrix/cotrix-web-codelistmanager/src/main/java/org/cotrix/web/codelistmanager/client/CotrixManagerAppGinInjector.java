package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.codelist.CodelistAttributesPanel;
import org.cotrix.web.codelistmanager.client.codelist.CodelistEditor;
import org.cotrix.web.codelistmanager.client.codelist.CodelistMetadataPanel;
import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelPresenter;
import org.cotrix.web.codelistmanager.client.codelists.CodelistsPresenter;
import org.cotrix.web.codelistmanager.client.manager.CodelistManagerPresenter;
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
	public CodelistsPresenter getCodeListPresenter();
	public CodelistManagerPresenter getCodeListManagerPresenter();
	
	public CodelistPanelPresenter getCodeListPanelPresenter();
	
	public CodelistEditor getCodeListEditor();
	public CodelistAttributesPanel getCodeListAttributesPanel();
	public CodelistMetadataPanel getCodeListMetadataPanel();
}
