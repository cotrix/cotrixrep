package org.acme.codelists;

import static junit.framework.Assert.*;
import static org.acme.codelists.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.common.Utils;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.junit.Test;

public class VersioningTest {

	//we answer the question: can DOs be correctly versioned?
	//this applies to codebags and codelists but requires all DOs to yield copies
	//so we mostly answer the question: can DOs be correctly copied?
	
	private Codelist.Private reveal(Codelist list) {
		return Utils.reveal(list,Codelist.Private.class);
	}
	
	@Test
	public void codelistsCanBeVersioned() {

		Code code = code().name(name).build();
		
		Codelist list = codelist().name(name).with(code).version(version).build(); 
				
		//a new version of the list
		Codelist versioned = reveal(list).bump("2");
				
		assertEquals("2",versioned.version());
		
		assertFalse(list.id().equals(versioned.id()));
				
		assertFalse(list.equals(versioned));

		assertFalse(list.codes().equals(versioned.codes()));
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void versionsMustBeConsistent() {
		
		Codelist list = codelist().name(name).version("2").build(); 
		
		//a failed attempt to version the list with an unacceptable number
		reveal(list).bump("1.3");
		
	}
	
	
	

}
