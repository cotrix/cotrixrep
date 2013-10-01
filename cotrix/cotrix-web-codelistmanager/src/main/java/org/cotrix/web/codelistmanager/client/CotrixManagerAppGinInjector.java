package org.cotrix.web.codelistmanager.client;

//import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinModule.AssistedInjectionFactory;
import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinModule.AssistedInjectionFactory;
import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenter;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenter;
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
	public CodeListPresenter getCodeListPresenter();
	public CodeListManagerPresenter getCodeListManagerPresenter();
	public AssistedInjectionFactory getFactory();
}
