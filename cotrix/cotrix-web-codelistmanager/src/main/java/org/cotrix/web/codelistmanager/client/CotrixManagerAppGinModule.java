package org.cotrix.web.codelistmanager.client;

import org.cotrix.web.codelistmanager.client.codelist.CodelistId;
import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelPresenter;
import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelPresenterImpl;
import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelView;
import org.cotrix.web.codelistmanager.client.codelist.CodelistPanelViewImpl;
import org.cotrix.web.codelistmanager.client.codelists.CodelistsPresenter;
import org.cotrix.web.codelistmanager.client.codelists.CodelistsPresenterImpl;
import org.cotrix.web.codelistmanager.client.codelists.CodelistsView;
import org.cotrix.web.codelistmanager.client.codelists.CodelistsViewImpl;
import org.cotrix.web.codelistmanager.client.data.CodeAttributeEditor;
import org.cotrix.web.codelistmanager.client.data.CodeEditor;
import org.cotrix.web.codelistmanager.client.data.MetadataAttributeEditor;
import org.cotrix.web.codelistmanager.client.data.MetadataEditor;
import org.cotrix.web.codelistmanager.client.data.ModifyCommandSequencer;
import org.cotrix.web.codelistmanager.client.di.CodeAttributeEditorProvider;
import org.cotrix.web.codelistmanager.client.di.CodeEditorProvider;
import org.cotrix.web.codelistmanager.client.di.CodelistIdProvider;
import org.cotrix.web.codelistmanager.client.di.CodelistPanelFactory;
import org.cotrix.web.codelistmanager.client.di.EditorEventBusProvider;
import org.cotrix.web.codelistmanager.client.di.MetadataAttributeEditorProvider;
import org.cotrix.web.codelistmanager.client.di.MetadataEditorProvider;
import org.cotrix.web.codelistmanager.client.event.EditorBus;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.manager.CodelistManagerPresenter;
import org.cotrix.web.codelistmanager.client.manager.CodelistManagerPresenterImpl;
import org.cotrix.web.codelistmanager.client.manager.CodelistManagerView;
import org.cotrix.web.codelistmanager.client.manager.CodelistManagerViewImpl;
import org.cotrix.web.codelistmanager.client.manager.ContentPanel;
import org.cotrix.web.codelistmanager.client.manager.ContentPanelController;
import org.cotrix.web.codelistmanager.client.util.Constants;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class CotrixManagerAppGinModule extends AbstractGinModule {
    
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
		
		bind(MetadataAttributeEditorProvider.class).in(Singleton.class);
		bind(MetadataAttributeEditor.class).toProvider(MetadataAttributeEditorProvider.class);
		
		bind(CodeEditorProvider.class).in(Singleton.class);
		bind(CodeEditor.class).toProvider(CodeEditorProvider.class);
		
		bind(CodeAttributeEditorProvider.class).in(Singleton.class);
		bind(CodeAttributeEditor.class).toProvider(CodeAttributeEditorProvider.class);
		
		bind(ModifyCommandSequencer.class).in(Singleton.class);
		bind(Constants.class).in(Singleton.class);
	}

}
