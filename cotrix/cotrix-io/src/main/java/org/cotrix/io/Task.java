package org.cotrix.io;

/**
 * A task governed by given {@link Directives}.
 * 
 * @author Fabio Simeoni
 *
 * @param <D> the type of {@link Directives}
 */
public interface Task<D extends Directives> {

	/**
	 * Return the runtime type of the {@link Directives} associated with this task.
	 * @return the runtime type of directives
	 */
	Class<? extends D> directedBy();
	
}
