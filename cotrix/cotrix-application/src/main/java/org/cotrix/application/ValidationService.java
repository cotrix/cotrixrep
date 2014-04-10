package org.cotrix.application;
import java.util.Collection;

import org.cotrix.common.Report;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;

public interface ValidationService {

	Report validate(Codelist list);
	
	Report validate(Codelist list, Collection<Code> codes);
	
	Report validate(Codelist codelist, Code ... codes);
}
