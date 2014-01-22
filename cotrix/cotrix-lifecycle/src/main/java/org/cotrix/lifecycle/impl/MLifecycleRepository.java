package org.cotrix.lifecycle.impl;

import static org.cotrix.common.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Singleton;

import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.LifecycleService;

/**
 * Memory-based implementation of {@link LifecycleService}
 * @author Fabio Simeoni
 *
 */
@Singleton @Alternative @Priority(DEFAULT)
public class MLifecycleRepository implements LifecycleRepository {

	private final Map<String,ResumptionToken> tokens = new HashMap<String, ResumptionToken>();
	
	@Override
	public void add(Lifecycle lc) {
		
		tokens.put(lc.resourceId(),new ResumptionToken(lc.name(),lc.state()));
		
	}
	
	@Override
	public ResumptionToken lookup(String id) {
		
		return tokens.get(id);
	}
	
	@Override
	public void update(Lifecycle lc) {
		
		ResumptionToken token = tokens.get(lc.resourceId());
		
		if (token==null)
			throw new AssertionError("attempt to update transient lifecycle "+lc.resourceId());
		
		token.state=lc.state();
	}
}
