package org.cotrix.application.validation;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.events.After;
import org.cotrix.common.events.Updated;
import org.cotrix.domain.codelist.Codelist;

@Singleton
public class CodelistObserver {
	
	@Inject
	DefaultValidationService validator;


	public void afterUpdate(@Observes @After @Updated Codelist.Private changeset){
		
		validator.checkPunctual(changeset);
		
	}
	

}
