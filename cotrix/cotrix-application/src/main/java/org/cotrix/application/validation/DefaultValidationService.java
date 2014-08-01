package org.cotrix.application.validation;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.application.ValidationService;
import org.cotrix.domain.codelist.Codelist;

@Singleton
public class DefaultValidationService implements ValidationService {

	@Inject
	private ValidationManager validator;
	
	@Override
	public void validate(Codelist list) {
		
		validator.checkBulk(list);
	}
	
}
