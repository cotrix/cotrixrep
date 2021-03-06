package org.cotrix.security.impl;

import static org.cotrix.common.Constants.*;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;

import org.cotrix.common.events.ApplicationLifecycleEvents.Shutdown;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Alternative @Priority(DEFAULT)
public class MRealm extends NativeRealm {

	private static Logger log = LoggerFactory.getLogger(MRealm.class);
	
	private Map<String,String> pwds = new HashMap<String,String>();
	
	@Override
	protected String passwordFor(String name) {
		return pwds.get(name);
	}
	
	@Override
	protected void create(String name, String pwd) {
		update(name,pwd); //no difference
	}
	
	@Override
	protected void update(String name, String pwd) {
		pwds.put(name,pwd);
	}
	
	void clear() {
		log.trace("clearing inner realm");
		pwds.clear();
	}
	
	public static void clear(@Observes Shutdown event, NativeRealm realm) {
		
		if (realm instanceof MRealm)
			MRealm.class.cast(realm).clear();
	}
}
