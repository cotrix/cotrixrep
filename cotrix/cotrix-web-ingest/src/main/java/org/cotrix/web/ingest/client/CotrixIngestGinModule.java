package org.cotrix.web.ingest.client;

import org.cotrix.web.ingest.client.event.ImportBus;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixIngestGinModule extends AbstractGinModule {

    protected void configure() {
    	bind(EventBus.class).annotatedWith(ImportBus.class).to(SimpleEventBus.class).in(Singleton.class);
    }
}
