package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelPresenter;
import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelPresenterImpl;
import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelView;
import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelViewImpl;
import org.cotrix.web.codelistmanager.client.codelist.CodelistId;
import org.cotrix.web.codelistmanager.client.codelists.CodelistsPresenter;
import org.cotrix.web.codelistmanager.client.codelists.CodelistsPresenterImpl;
import org.cotrix.web.codelistmanager.client.codelists.CodelistsView;
import org.cotrix.web.codelistmanager.client.codelists.CodelistsViewImpl;
import org.cotrix.web.codelistmanager.client.data.CodelistRowEditor;
import org.cotrix.web.codelistmanager.client.data.MetadataEditor;
import org.cotrix.web.codelistmanager.client.di.CodelistPanelFactory;
import org.cotrix.web.codelistmanager.client.di.CodelistRowEditorProvider;
import org.cotrix.web.codelistmanager.client.di.CodelistIdProvider;
import org.cotrix.web.codelistmanager.client.di.EditorEventBusProvider;
import org.cotrix.web.codelistmanager.client.di.MetadataEditorProvider;
import org.cotrix.web.codelistmanager.client.event.EditorBus;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.manager.CodelistManagerPresenter;
import org.cotrix.web.codelistmanager.client.manager.CodelistManagerPresenterImpl;
import org.cotrix.web.codelistmanager.client.manager.CodelistManagerView;
import org.cotrix.web.codelistmanager.client.manager.CodelistManagerViewImpl;
import org.cotrix.web.codelistmanager.client.manager.ContentPanel;
import org.cotrix.web.codelistmanager.client.manager.ContentPanelController;

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
	    
		bind(CodelistManagerPresenter.class).to(CodelistManagerPresenterImpl.class);
		bind(CodelistManagerView.class).to(CodelistManagerViewImpl.class);
	
		bind(CodelistsPresenter.class).to(CodelistsPresenterImpl.class);
		bind(CodelistsView.class).to(CodelistsViewImpl.class);
		
		bind(ContentPanelController.class).in(Singleton.class);
		bind(ContentPanel.class).in(Singleton.class);

		bind(CodelistPanelView.class).to(CodelistPanelViewImpl.class);
		
		bind(CodelistPanelPresenter.class).to(CodelistPanelPresenterImpl.class);
		bind(CodelistPanelFactory.class).in(Singleton.class);
		
		bind(EditorEventBusProvider.class).in(Singleton.class);
		bind(EventBus.class).annotatedWith(EditorBus.class).toProvider(EditorEventBusProvider.class);
		
		bind(CodelistIdProvider.class).in(Singleton.class);
		bind(String.class).annotatedWith(CodelistId.class).toProvider(CodelistIdProvider.class);
		
		bind(MetadataEditorProvider.class).in(Singleton.class);
		bind(MetadataEditor.class).toProvider(MetadataEditorProvider.class);
		
		bind(CodelistRowEditorProvider.class).in(Singleton.class);
		bind(CodelistRowEditor.class).toProvider(CodelistRowEditorProvider.class);
	}

}
