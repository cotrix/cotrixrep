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
	 * @throws IllegalStateException if no lifecycle exists with the given name
	 */
	Lifecycle start(String name, String id);
	
	
	/**
	 * Start a given lifecycle in a given state for a given resource
	 * @param name the name of the lifecycle
	 * @param id the identifier of the resource
	 * @param startState the initial state of the lifecycle
	 * @return a lifecycle with the given name for the resource with the given identifier 
	 * 
	 * @throws IllegalStateException if no lifecycle exists with the given name
	 * @throws IllegalArgumentException if the start state is illegal for a lifecycle with the given name
	 */
	Lifecycle start(String name, String id, State startState);
	
	
	/**
	 * Start a default lifecycle for a given resource
	 * @param id the identifier of the resource
	 * @return a default lifecycle for the resource with the given identifier 
	 * 
	 */
	Lifecycle start(String id);
	
	/**
	 * Start a default lifecycle in a given state for a given resource
	 * @param id the identifier of the resource
	 * @param startState the initial state of the lifecycle 
	 * @return a default lifecycle for the resource with the given identifier 
	 * 
	 * @throws IllegalArgumentException if the start state is illegal for the default lifecycle
	 */
	Lifecycle start(String id, State startState);
	
	/**
	 * Returns the lifecycle of a given resource
	 * @param id the identifier of the resource
	 * @return the lifecycle of the resource with the given identifier
	 * 
	 * @throws IllegalStateException if no lifecycle exists for the given resource
	 */
	Lifecycle lifecycleOf(String id);
	
	
	void update(Lifecycle lc);
	
}
