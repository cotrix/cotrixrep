package org.cotrix.application;

import org.cotrix.domain.codelist.Codelist;


//triggers bulk validation
public interface ValidationService {

	void validate(Codelist list);
}
