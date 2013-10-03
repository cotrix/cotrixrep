package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinModule.AssistedInjectionFactory;
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
	public AssistedInjectionFactory getFactory();
}
