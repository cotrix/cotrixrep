package org.cotrix.lifecycle.impl;

import static org.cotrix.common.Utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.lifecycle.LifecycleFactory;
import org.cotrix.lifecycle.LifecycleRegistry;

/**
 * Memory-based implementation of {@link LifecycleRegistry}
 * 
 * @author Fabio Simeoni
 *
 */
@Singleton
public class MemoryLifecycleRegistry implements LifecycleRegistry {

	private final Map<String,LifecycleFactory> factories = new HashMap<String, LifecycleFactory>();
	
	@Inject
	public MemoryLifecycleRegistry(List<? extends LifecycleFactory> factories) {
		
		notNull("lifecycle factories",factories);
		
		for (LifecycleFactory factory : factories)
			this.factories.put(factory.name(),factory);
	}
	
	@Override
	public LifecycleFactory get(String name) {
		
		valid("factory name", name);
		
		return factories.get(name);
	}
	
	
	@Override
	public void add(LifecycleFactory factory) {
		
		notNull("lifecycle factory",factory);
		
		factories.put(factory.name(),factory);
	}
}
