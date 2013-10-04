package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelPresenter;
import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelPresenterImpl;
import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelView;
import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelViewImpl;
import org.cotrix.web.codelistmanager.client.codelist.CodeListRowDataProvider;
import org.cotrix.web.codelistmanager.client.codelists.CodeListsPresenter;
import org.cotrix.web.codelistmanager.client.codelists.CodeListsPresenterImpl;
import org.cotrix.web.codelistmanager.client.codelists.CodeListsView;
import org.cotrix.web.codelistmanager.client.codelists.CodeListsViewImpl;
import org.cotrix.web.codelistmanager.client.data.MetadataProvider;
import org.cotrix.web.codelistmanager.client.data.MetadataSaver;
import org.cotrix.web.codelistmanager.client.event.EditorBus;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.manager.CodeListManagerPresenter;
import org.cotrix.web.codelistmanager.client.manager.CodeListManagerPresenterImpl;
import org.cotrix.web.codelistmanager.client.manager.CodeListManagerView;
import org.cotrix.web.codelistmanager.client.manager.CodeListManagerViewImpl;
import org.cotrix.web.codelistmanager.client.manager.ContentPanel;
import org.cotrix.web.codelistmanager.client.manager.ContentPanelController;
import org.cotrix.web.codelistmanager.client.old.CodeListDetailPresenter;
import org.cotrix.web.codelistmanager.client.old.CodeListDetailPresenterImpl;
import org.cotrix.web.codelistmanager.client.old.CodeListDetailView;
import org.cotrix.web.codelistmanager.client.old.CodeListDetailViewImpl;
import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
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
	
		bind(CodeListsPresenter.class).to(CodeListsPresenterImpl.class);
		bind(CodeListsView.class).to(CodeListsViewImpl.class);
		
		bind(ContentPanelController.class).in(Singleton.class);
		bind(ContentPanel.class).in(Singleton.class);

		bind(CodeListPanelView.class).to(CodeListPanelViewImpl.class);
		
		bind(CodeListDetailView.class).to(CodeListDetailViewImpl.class);
		bind(CodeListDetailPresenter.class).to(CodeListDetailPresenterImpl.class);
		
		bind(EventBus.class).annotatedWith(EditorBus.class).to(SimpleEventBus.class).in(Singleton.class);
		
		install(new GinFactoryModuleBuilder().implement(CodeListPanelPresenter.class, CodeListPanelPresenterImpl.class).build(AssistedInjectionFactory.class));
	}
	
	public interface AssistedInjectionFactory {
		public CodeListRowDataProvider createCodeListRowDataProvider(String codelistId);
		public MetadataProvider createMetadataProvider(String codelistId);
		public MetadataSaver createMetadataSaver(String codelistId);
	    public CodeListPanelPresenter createCodeListPanelPresenter(UICodelist codelist);
	}
	

}
