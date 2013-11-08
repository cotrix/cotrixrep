package org.cotrix.io.utils;

import java.util.HashMap;
import java.util.Map;

import org.cotrix.io.impl.Task;

/**
 * A collection of directive-driven tasks, indexed by directive type.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of tasks
 */
public class Registry<T extends Task<?>> {

	private final Map<Class<?>,T> tasks = new HashMap<Class<?>,T>();
	
	/**
	 * Creates an instance with given tasks.
	 * @param tasks the tasks
	 */
	public Registry(Iterable<T> tasks) {
		
		for (T task : tasks)
			this.tasks.put(task.directedBy(),task);
	}
	
	/**
	 * Returns the task in this registry which is driven by given directives.
	 * @param directives the directives
	 * @return the task
	 * 
	 */
	public T get(Object directives) {
		
		T s =  tasks.get(directives.getClass());
		
		if (s==null)
			throw new AssertionError("no task for directives "+directives.getClass());
		
		return s;
	}
	
	/**
	 * Returns all the tasks in this registry.
	 * @return the tasks
	 */
	public Iterable<T> tasks() {
		return tasks.values();
	}
}
