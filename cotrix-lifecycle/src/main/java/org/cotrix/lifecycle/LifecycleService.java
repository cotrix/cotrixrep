package org.cotrix.lifecycle;

/**
 * Starts and returns resource {@link Lifecycle}s. 
 * 
 * @author Fabio Simeoni
 *
 */
public interface LifecycleService {

	/**
	 * Start a given lifecycle for a given resource
	 * @param name the name of the lifecycle
	 * @param id the identifier of the resource
	 * @return a lifecycle with the given name for the resource with the given identifier 
	 * 
	 * @throws IllegalStateException if no lifecycle exists with the given name and/or for the given resource
	 */
	Lifecycle start(String name, String id);
	
	
	/**
	 * Start a default lifecycle for a given resource
	 * @param id the identifier of the resource
	 * @return a default lifecycle for the resource with the given identifier 
	 * 
	 * @throws IllegalStateException if no default lifecycle exists for the given resource
	 */
	Lifecycle start(String id);
	
	/**
	 * Returns the lifecycle of a given resource
	 * @param id the identifier of the resource
	 * @return the lifecycle of the resource with the given identifier
	 * 
	 * @throws IllegalStateException if no lifecycle exists for the given resource
	 */
	Lifecycle lifecycleOf(String id);
	
}
