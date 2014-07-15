package org.cotrix.common.async;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskContext {

	private static InheritableThreadLocal<Task> context = new InheritableThreadLocal<Task>() {
		protected Task initialValue() {
			return new Task();
		}
	};
	
	Task thisTask() {
		return context.get();
	}
	
	//encapsulate cooperative cancellation mechanism for clarity and future evolution
	public boolean isCancelled() {
		return Thread.currentThread().isInterrupted();
	}
	
	public void save(Object o) {
		thisTask().put(o);
	}
	
	void reset() {
		context.remove();
	}
}
