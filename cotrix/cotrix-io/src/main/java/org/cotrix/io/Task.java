package org.cotrix.io;

/**
 * An input/output task directed by given {@link Directives}.
 * 
 * @author Fabio Simeoni
 *
 * @param <D> the type of {@link Directives}
 */
public interface Task<D extends Directives> {

	/**
	 * Return the type of {@link Directives} associated with this task.
	 * @return the mockDirectives
	 */
	Class<? extends D> directedBy();
	
}
