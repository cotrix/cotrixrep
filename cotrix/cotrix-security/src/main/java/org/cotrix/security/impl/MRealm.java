package org.cotrix.security.impl;

import static org.cotrix.common.Constants.*;
import static org.cotrix.domain.dsl.Users.*;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;

import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.security.Realm;
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
		pwds.put(name,pwd);
	}
	
	public void clear(@Observes Startup event) {
		pwds.put(cotrix.name(),cotrix.name());

	}
	
	public void clear(@Observes Shutdown event, @Native Realm realm) {
		if (this.getClass().isInstance(realm)) {
			log.trace("clearing inner realm");
			pwds.clear();
		}
	}
}
