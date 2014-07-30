package org.cotrix.application.validation;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.events.Modified;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.impl.EventProducer.UpdateActionEvent;

@Singleton
public class CodelistObserver {
	
	@Inject
	ValidationManager validator;

	public void onCodelistUpdate(@Observes @Modified Codelist changeset){
		
		validator.check(changeset);
		
	}
	
	public void onCodelistBulkUpdate(@Observes @Modified UpdateActionEvent event){
		
		validator.check(event.id);
		
	}
}
