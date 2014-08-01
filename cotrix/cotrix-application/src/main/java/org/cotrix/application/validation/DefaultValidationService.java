package org.cotrix.application.validation;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.application.ValidationService;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;

@Singleton
public class DefaultValidationService implements ValidationService {

	@Inject
	CodelistRepository codelists;
	
	@Inject
	ValidationManager validator;
	
	@Override
	public void validate(Codelist list) {
		
		validator.checkBulk(list);
	}
	
}
