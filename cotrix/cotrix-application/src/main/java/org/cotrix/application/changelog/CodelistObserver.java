package org.cotrix.application.changelog;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.events.Modified;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.impl.EventProducer.UpdateActionEvent;

@Singleton
public class CodelistObserver {
	
	@Inject
	ChangelogManager log;

	public void onCodelistUpdate(@Observes @Modified Codelist changeset){
		
		log.updateWith(changeset);
		
	}
	
	public void onCodelistBulkUpdate(@Observes @Modified UpdateActionEvent event){
		
		log.update(event.id);
		
	}
}
