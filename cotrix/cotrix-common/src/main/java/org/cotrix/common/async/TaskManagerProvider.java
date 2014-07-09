package org.cotrix.common.async;

public interface TaskManagerProvider {

	public interface TaskManager {
		
		void started();
		
		void finished();

	}
	
	
	TaskManager get();
	
	
}
