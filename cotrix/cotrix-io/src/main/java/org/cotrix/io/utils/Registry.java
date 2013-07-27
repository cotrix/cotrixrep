package org.cotrix.io.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cotrix.io.Directives;
import org.cotrix.io.Task;

/**
 * A collection of {@link Task}s, indexed by their {@link Directives}.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of task
 */
public class Registry<T extends Task<?>> {

	private final Map<Class<?>,T> tasks = new HashMap<Class<?>,T>();
	
	/**
	 * Creates an instance with given {@link Task}s.
	 * @param tasks the tasks
	 */
	public Registry(Collection<? extends T> tasks) {
		for (T task : tasks)
			this.tasks.put(task.directedBy(),task);
	}
	
	/**
	 * Returns the {@link Task} associated with given {@link Directives} in this registry.
	 * @param mockDirectives the mockDirectives
	 * @return the task
	 */
	public T get(Directives directives) {
		T s =  tasks.get(directives.getClass());
		if (s==null)
			throw new IllegalStateException("type "+directives.getClass()+" is unbound in this registry");
		return s;
	}
	
	/**
	 * Returns all the {@link Task}s in this registry.
	 * @return
	 */
	public Collection<T> getAll() {
		return tasks.values();
	}
}
