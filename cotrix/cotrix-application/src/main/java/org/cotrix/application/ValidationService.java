package org.cotrix.application;

import org.cotrix.domain.codelist.Codelist;

public interface ValidationService {

	
	void validate(Codelist list);
	
	void validate(String codelistId);
}
