package org.cotrix.domain.utils;

import javax.enterprise.event.Observes;

import org.cotrix.common.events.ApplicationLifecycleEvents.Startup;

//provide a singleton engine statically
//a DI bridge for domain objects that are not managed beans.
public class ScriptEngineProvider {

	static ScriptEngine engine;
	
	public static ScriptEngine engine() {
		return engine;
	}
	
	//configured at startup
	static void onStart(@Observes Startup startup,ScriptEngine e) {
		engine = e;
	}
}
