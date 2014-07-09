package org.acme;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

import org.cotrix.common.async.TaskEvents.EndTask;
import org.cotrix.common.async.TaskEvents.StartTask;
import org.cotrix.common.async.TaskEvents.StartTask.InfoProvider;
import org.cotrix.common.async.TaskEvents.StartTask.TaskInfo;
import org.cotrix.common.events.Current;
import org.jboss.weld.context.bound.BoundSessionContext;

public class CdiProducers {

	
	
	
	
	

	@Produces @ApplicationScoped @Current
	static Map<String, Object> sessionStorage() {
		return new HashMap<>();
	}
	
	@Produces @ApplicationScoped @Current
	static InfoProvider infoProvider() {
		return new InfoProvider() {
			
			@Override
			public TaskInfo get() {
				return null;
			}
		};
	}

	static void endTask(@Observes EndTask event, @Current Map<String, Object> map, BoundSessionContext ctx) {
			
		ctx.dissociate(map);
	}

	static void startTask(@Observes StartTask event, @Current Map<String, Object> map, BoundSessionContext ctx) {

		ctx.associate(map);
		ctx.activate();

	}
}
