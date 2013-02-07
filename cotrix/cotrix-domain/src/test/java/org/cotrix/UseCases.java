package org.cotrix;

import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.codes.Codelist;
import org.junit.Test;

public class UseCases {

	@Test
	public void buildSimpleList() {
		
		Codelist mylist = codelist(q("mylist"))
									.with(a(q("name"),"value"), 
										  a(q("name"),q("type"),"value"))
									.with(ascode("code1"),
										  ascode("code2"))
									.build();
		
		System.out.println(mylist);
	}
	
	@Test
	public void modelAsfis() {
		//TODO
	}
	
	@Test
	public void modelXXX() {
		//TODO
	}
}
