/**
 * 
 */
package org.cotrix.web.manage.client.di;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistBusProvider implements Provider<EventBus> {

	protected EventBus eventBus;

	public void generate() {
		this.eventBus = new SimpleEventBus();
	}

	@Override
	public EventBus get() {

		return eventBus;
	}

}
