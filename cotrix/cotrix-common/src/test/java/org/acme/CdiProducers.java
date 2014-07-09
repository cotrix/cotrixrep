package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.cotrix.common.async.TaskManager;
import org.cotrix.common.events.Current;

public class CdiProducers {

		
	@Produces @ApplicationScoped @Current
	TaskManager testManager() {
		
		return new TaskManager() {
			
			@Override
			public void submitted() {}
			
			@Override
			public void started() {}
			
			@Override
			public void finished() {}
		};
	}
	
}
