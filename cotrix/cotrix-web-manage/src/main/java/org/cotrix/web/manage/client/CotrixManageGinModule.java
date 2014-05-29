package org.cotrix.web.manage.client;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.codelist.common.AttributesPanel;
import org.cotrix.web.manage.client.di.CodelistProvider;
import org.cotrix.web.manage.client.di.CurrentCodelist;
import org.cotrix.web.manage.client.di.CodelistIdProvider;
import org.cotrix.web.manage.client.di.EditorEventBusProvider;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.client.util.Attributes;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixManageGinModule extends AbstractGinModule {
    
	@Override
	protected void configure() {
		bind(EventBus.class).annotatedWith(ManagerBus.class).to(SimpleEventBus.class).in(Singleton.class);
		
		bind(EventBus.class).annotatedWith(EditorBus.class).toProvider(EditorEventBusProvider.class);
		
		bind(String.class).annotatedWith(CurrentCodelist.class).toProvider(CodelistIdProvider.class);
		bind(UICodelist.class).annotatedWith(CurrentCodelist.class).toProvider(CodelistProvider.class);
		
		requestStaticInjection(AttributesPanel.class);
		requestStaticInjection(Attributes.class);
	}
}
