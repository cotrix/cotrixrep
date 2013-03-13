package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenter;
import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenterImpl;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenter;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenterImpl;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerView;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerViewImpl;
import org.cotrix.web.codelistmanager.client.view.CodeListView;
import org.cotrix.web.codelistmanager.client.view.CodeListViewImpl;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class AppGinModule extends AbstractGinModule{


    @Provides
    @Singleton
    public HandlerManager getEventBus() {
        return new HandlerManager(null);
    }
    
	@Override
	protected void configure() {
		bind(CodelistManagerAppController.class).to(CodelistManagerAppControllerImpl.class);
	      
		bind(CodeListManagerView.class).to(CodeListManagerViewImpl.class);
		bind(CodeListManagerPresenter.class).to(CodeListManagerPresenterImpl.class);
	
		bind(CodeListView.class).to(CodeListViewImpl.class);
		bind(CodeListPresenter.class).to(CodeListPresenterImpl.class);
	}

}
