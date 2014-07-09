package org.acme;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.cotrix.common.async.TaskManager;
import org.cotrix.common.events.Current;
import org.jboss.weld.context.bound.BoundSessionContext;

public class CdiProducers {

	
	
	@Produces @ApplicationScoped @Current
	TaskManager testManager(final BoundSessionContext ctx, @Current final Map<String, Object> storage ) {
		
		return new TaskManager() {
			
			@Override
			public void submitted() {}
			
			@Override
			public void started() {
				ctx.associate(storage);
				ctx.activate();
				
			}
			
			@Override
			public void finished() {
				ctx.dissociate(storage);
			}
		};
	}
	
	

	@Produces @ApplicationScoped @Current
	static Map<String, Object> sessionStorage() {
		return new HashMap<>();
	}
}
