package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenter;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(CotrixManagerAppGinModule.class)
public interface CotrixManagerAppGinInjector extends Ginjector {
	    public ManagerServiceAsync getRpcService();
	    public CotrixManagerAppController getAppController();
	    public CodeListPresenter getCodeListPresenter();
	    public CodeListManagerPresenter getCodeListManagerPresenter();
}
