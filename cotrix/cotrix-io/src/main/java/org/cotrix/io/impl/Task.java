package org.cotrix.io.impl;

/**
 * A task driven by directives.
 * 
 * @author Fabio Simeoni
 *
 * @param <D> the type of directives
 */
public interface Task<D> {

	//this serves as the common basis for task-specific extensions (e.g to parse, map, serialise).
	//directives types are used by registries to dispatch to tasks
	
	/**
	 * Return the type of directives that drive this task.
	 * @return the type of directives that drive this task
	 */
	Class<D> directedBy();
	
}
