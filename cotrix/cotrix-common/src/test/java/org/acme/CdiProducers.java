package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.cotrix.common.async.TaskManagerProvider;

public class CdiProducers {

		
	@Produces @ApplicationScoped
	TaskManagerProvider testManagerProvider() {
		
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
