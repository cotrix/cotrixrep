package org.cotrix;

import static org.cotrix.Fixture.*;
import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.Codelist;
import org.cotrix.domain.Factory;
import org.cotrix.domain.simple.SimpleFactory;
import org.junit.Test;

public class UseCases {

	static Factory factory = new SimpleFactory();
	
	@Test
	public void buildSimpleList() {
		
		Codelist mylist = codelist().with(q("mylist"))
									.with(ascode("code1"),ascode("code2"))
									.and(a,a)
									.build();
		
		System.out.println(mylist);
	}
	
	@Test
	public void versionSimpleList() {
		
		Codelist mylist = codelist().with(q("mylist"))
									.with(ascode("code1"),ascode("code2"))
									.and(a,a)
									.version("1")
									.build();
		
		Codelist mylist2 = version(mylist).as("2");
		
		mylist2.codes().add(ascode("code3"));
		
		System.out.println(mylist2);
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
