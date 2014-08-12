package org.cotrix.application.changelog;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.events.After;
import org.cotrix.common.events.Before;
import org.cotrix.common.events.New;
import org.cotrix.common.events.Updated;
import org.cotrix.domain.codelist.Codelist;

@Singleton
public class CodelistObserver {
	
	@Inject
	DefaultChangelogService log;

	public void beforeAdd(@Observes @Before @New Codelist list){
		
		CREATED.set(time()).on(list);
		
	}

	public void afterUpdate(@Observes @After @Updated Codelist.Private changeset){
		
		log.trackAfter(changeset);
		
	}
}
