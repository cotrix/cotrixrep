package org.cotrix.application.managed;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.utils.CodeStatus.*;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.utils.CodeStatus;


//read-only interface over codes with support for systemic attributes
public class ManagedCode extends ManagedEntity<Code> {

	
	public static ManagedCode manage(Code code) {
		return new ManagedCode(code);
	}
	
	ManagedCode(Code code) {
		super(code);
	}
	
	public CodeStatus status() {
		
		String val = lookup(STATUS);
		
		if (val==null)
			return VALID;
		
		return CodeStatus.valueOf(val);
	}
	
	
}
