package org.cotrix.lifecycle.impl;

import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.State;

public interface LifecycleRepository {

	static class ResumptionToken {
		
		public final String name;
		public State state;
		
		public ResumptionToken(String name, State state) {
			this.name=name;
			this.state=state;
		}
	}
	
	void add(Lifecycle lc);
	
	ResumptionToken lookup(String id);
	
	void update(Lifecycle lc);
	
	void delete(Lifecycle lc);
	
	
}
