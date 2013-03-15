package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenter;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(AppGinModule.class)
public interface AppGinInjector extends Ginjector {
	    public ManagerServiceAsync getRpcService();
	    public CodelistManagerAppController getAppController();
	    public CodeListPresenter getCodeListPresenter();
	    public CodeListManagerPresenter getCodeListManagerPresenter();
}
