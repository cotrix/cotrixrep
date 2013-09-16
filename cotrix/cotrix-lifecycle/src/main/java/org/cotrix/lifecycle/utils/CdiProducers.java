package org.cotrix.lifecycle.utils;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.lifecycle.LifecycleFactory;

/**
 * CDI producers
 * @author Fabio Simeoni
 *
 */
public class CdiProducers {

	@Inject
	Instance<LifecycleFactory> factories;
	
	
	
	@Produces 
	List<LifecycleFactory> factories() {
		
		List<LifecycleFactory> factories = new ArrayList<LifecycleFactory>();
		for (LifecycleFactory factory : this.factories)
			factories.add(factory);
		
		return factories;
	}
	
	
}
