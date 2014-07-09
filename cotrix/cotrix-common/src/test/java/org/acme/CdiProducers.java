package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.cotrix.common.async.TaskManagerProvider;
import org.cotrix.common.events.Current;

public class CdiProducers {

		
	@Produces @ApplicationScoped @Current
	TaskManagerProvider testManager() {
		
		return new TaskManagerProvider() {
			
			@Override
			public TaskManager get() {
				
				return new TaskManager() {
					
					@Override
					public void started() {
						System.out.println("started task manager");
					}
					
					@Override
					public void finished() {
						System.out.println("ended task manager");
					}
				};
			}
			
			
		};
	}
	
}
