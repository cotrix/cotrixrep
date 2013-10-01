package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.presenter.CodeListDetailPresenter;
import org.cotrix.web.codelistmanager.client.presenter.CodeListDetailPresenterImpl;
import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenter;
import org.cotrix.web.codelistmanager.client.presenter.CodeListManagerPresenterImpl;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPanelPresenter;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPanelPresenterImpl;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenter;
import org.cotrix.web.codelistmanager.client.presenter.CodeListPresenterImpl;
import org.cotrix.web.codelistmanager.client.presenter.ContentPanelController;
import org.cotrix.web.codelistmanager.client.view.CodeListDetailView;
import org.cotrix.web.codelistmanager.client.view.CodeListDetailViewImpl;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerView;
import org.cotrix.web.codelistmanager.client.view.CodeListManagerViewImpl;
import org.cotrix.web.codelistmanager.client.view.CodeListPanelView;
import org.cotrix.web.codelistmanager.client.view.CodeListPanelViewImpl;
import org.cotrix.web.codelistmanager.client.view.CodeListView;
import org.cotrix.web.codelistmanager.client.view.CodeListViewImpl;
import org.cotrix.web.codelistmanager.client.view.ContentPanel;
import org.cotrix.web.share.client.event.FeatureAsyncCallBack;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.client.feature.FeatureBus;
import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.inject.client.binder.GinBinder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class CotrixManagerAppGinModule extends AbstractGinModule {

    @Provides
    @Singleton
    public HandlerManager getEventBus() {
    	//FIXME remove
        return new HandlerManager(null);
    }
    
	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(ManagerBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(CotrixManagerAppController.class).to(CotrixManagerAppControllerImpl.class);
	    
		bind(CodeListManagerPresenter.class).to(CodeListManagerPresenterImpl.class);
		bind(CodeListManagerView.class).to(CodeListManagerViewImpl.class);
	
		bind(CodeListPresenter.class).to(CodeListPresenterImpl.class);
		bind(CodeListView.class).to(CodeListViewImpl.class);
		
		bind(ContentPanelController.class).in(Singleton.class);
		bind(ContentPanel.class).in(Singleton.class);
		
		//bind(CodeListPanelPresenter.class).to(CodeListPanelPresenterImpl.class);
		bind(CodeListPanelView.class).to(CodeListPanelViewImpl.class);
		
		bind(CodeListDetailView.class).to(CodeListDetailViewImpl.class);
		bind(CodeListDetailPresenter.class).to(CodeListDetailPresenterImpl.class);
		
		install(new GinFactoryModuleBuilder().implement(CodeListPanelPresenter.class, CodeListPanelPresenterImpl.class).build(AssistedInjectionFactory.class));
	}
	
	public interface AssistedInjectionFactory {
	    public CodeListPanelPresenter createCodeListPanelPresenter(UICodelist codelist);
	}
	

}
