package org.cotrix.web.manage.client;

import org.cotrix.web.manage.client.codelist.CodeFactory;
import org.cotrix.web.manage.client.codelist.CodelistId;
import org.cotrix.web.manage.client.codelist.CodelistPanelPresenter;
import org.cotrix.web.manage.client.codelist.CodelistPanelPresenterImpl;
import org.cotrix.web.manage.client.codelist.CodelistPanelView;
import org.cotrix.web.manage.client.codelist.CodelistPanelViewImpl;
import org.cotrix.web.manage.client.codelist.attribute.AttributeFactory;
import org.cotrix.web.manage.client.codelists.CodelistsDataProvider;
import org.cotrix.web.manage.client.codelists.CodelistsPresenter;
import org.cotrix.web.manage.client.codelists.CodelistsPresenterImpl;
import org.cotrix.web.manage.client.codelists.CodelistsView;
import org.cotrix.web.manage.client.codelists.CodelistsViewImpl;
import org.cotrix.web.manage.client.data.ModifyCommandSequencer;
import org.cotrix.web.manage.client.di.CodelistIdProvider;
import org.cotrix.web.manage.client.di.CodelistPanelFactory;
import org.cotrix.web.manage.client.di.EditorEventBusProvider;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.client.manager.CodelistManagerPresenter;
import org.cotrix.web.manage.client.manager.CodelistManagerPresenterImpl;
import org.cotrix.web.manage.client.manager.CodelistManagerView;
import org.cotrix.web.manage.client.manager.CodelistManagerViewImpl;
import org.cotrix.web.manage.client.manager.ContentPanel;
import org.cotrix.web.manage.client.manager.ContentPanelController;
import org.cotrix.web.manage.client.util.Constants;

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
		bind(CodelistsDataProvider.class).in(Singleton.class);
		
		bind(ContentPanelController.class).in(Singleton.class);
		bind(ContentPanel.class).in(Singleton.class);

		bind(CodelistPanelView.class).to(CodelistPanelViewImpl.class);
		
		bind(CodelistPanelPresenter.class).to(CodelistPanelPresenterImpl.class);
		bind(CodelistPanelFactory.class).in(Singleton.class);
		
		bind(EditorEventBusProvider.class).in(Singleton.class);
		bind(EventBus.class).annotatedWith(EditorBus.class).toProvider(EditorEventBusProvider.class);
		
		bind(CodelistIdProvider.class).in(Singleton.class);
		bind(String.class).annotatedWith(CodelistId.class).toProvider(CodelistIdProvider.class);
		
		bind(ModifyCommandSequencer.class).in(Singleton.class);
		bind(Constants.class).in(Singleton.class);
		
		requestStaticInjection(AttributeFactory.class);
		requestStaticInjection(CodeFactory.class);
	}

}
