package org.cotrix.repository.impl.memory;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationLifecycle;
import org.cotrix.common.cdi.ApplicationEvents.Startup;

public class MemoryStoreLifecycle {

	//acts on behalf of a hypothetical memory store and let app know it's always a fresh start
	void init(@Observes Startup event, ApplicationLifecycle app) {
		app.isFirstStart();
	}
}
