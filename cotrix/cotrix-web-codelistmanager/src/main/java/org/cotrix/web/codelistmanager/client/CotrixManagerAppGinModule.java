package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelPresenter;
import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelPresenterImpl;
import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelView;
import org.cotrix.web.codelistmanager.client.codelist.CodeListPanelViewImpl;
import org.cotrix.web.codelistmanager.client.codelist.CodelistId;
import org.cotrix.web.codelistmanager.client.codelists.CodeListsPresenter;
import org.cotrix.web.codelistmanager.client.codelists.CodeListsPresenterImpl;
import org.cotrix.web.codelistmanager.client.codelists.CodeListsView;
import org.cotrix.web.codelistmanager.client.codelists.CodeListsViewImpl;
import org.cotrix.web.codelistmanager.client.data.CodeListRowEditor;
import org.cotrix.web.codelistmanager.client.data.MetadataEditor;
import org.cotrix.web.codelistmanager.client.di.CodeListPanelFactory;
import org.cotrix.web.codelistmanager.client.di.CodeListRowEditorProvider;
import org.cotrix.web.codelistmanager.client.di.CodelistIdProvider;
import org.cotrix.web.codelistmanager.client.di.EditorEventBusProvider;
import org.cotrix.web.codelistmanager.client.di.MetadataEditorProvider;
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

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
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
		
		bind(CodeListPanelPresenter.class).to(CodeListPanelPresenterImpl.class);
		bind(CodeListPanelFactory.class).in(Singleton.class);
		
		bind(EditorEventBusProvider.class).in(Singleton.class);
		bind(EventBus.class).annotatedWith(EditorBus.class).toProvider(EditorEventBusProvider.class);
		
		bind(CodelistIdProvider.class).in(Singleton.class);
		bind(String.class).annotatedWith(CodelistId.class).toProvider(CodelistIdProvider.class);
		
		bind(MetadataEditorProvider.class).in(Singleton.class);
		bind(MetadataEditor.class).toProvider(MetadataEditorProvider.class);
		
		bind(CodeListRowEditorProvider.class).in(Singleton.class);
		bind(CodeListRowEditor.class).toProvider(CodeListRowEditorProvider.class);
	}

}
