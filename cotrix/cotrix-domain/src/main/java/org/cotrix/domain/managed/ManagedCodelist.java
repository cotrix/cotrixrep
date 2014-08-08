package org.cotrix.domain.managed;

import static org.cotrix.domain.attributes.CommonDefinition.*;

import org.cotrix.domain.codelist.Codelist;


public class ManagedCodelist extends ManagedEntity<Codelist> {

	
	public static ManagedCodelist manage(Codelist list) {
		return new ManagedCodelist(list);
	}
	
	
	public ManagedCodelist() {
	}
	
	public ManagedCodelist(Codelist list) {
		super(list);
	}
	
	public String previousVersion() {
		
		String val = lookup(PREVIOUS_VERSION);
		
		return val== null ? null : String.valueOf(val);
		
	}
	
	
	
}
