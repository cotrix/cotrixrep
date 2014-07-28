package org.cotrix.application.changelog;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.events.Modified;
import org.cotrix.domain.codelist.Codelist;

@Singleton
public class CodelistObserver {
	
	@Inject
	Changelog log;

	public void onCodelistUpdate(@Observes @Modified Codelist changeset){
		
		log.updateWith(changeset);
		
	}
}