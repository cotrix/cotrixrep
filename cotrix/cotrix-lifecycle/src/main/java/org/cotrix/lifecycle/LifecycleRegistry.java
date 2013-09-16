package org.cotrix.lifecycle;


/**
 * A repository of {@link Lifecycle} factories.
 * 
 * @author Fabio Simeoni
 *
 */
public interface LifecycleRegistry {

	/**
	 * Returns a given {@link LifecycleFactory}
	 * @param name the name of the factory
	 * @return the factory with the given name
	 */
	LifecycleFactory get(String name);
	
	/**
	 * Adds a {@link LifecycleFactory}.
	 * 
	 * @param factory the factory
	 */
	void add(LifecycleFactory factory);
	
}
