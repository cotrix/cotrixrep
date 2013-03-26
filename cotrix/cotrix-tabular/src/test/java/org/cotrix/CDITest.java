package org.cotrix;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.inject.Inject;

import org.cotrix.domain.Codelist;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.importservice.Directives;
import org.cotrix.importservice.ImportService;
import org.cotrix.importservice.Outcome;
import org.cotrix.importservice.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class CDITest {

	@Inject
	ImportService service;
	
	@Test
	public void importSimpleCodelist() throws Exception {

		InputStream mockData = new ByteArrayInputStream(new byte[0]);
		Directives<Codelist> mockDirectives = new TestDirectives();
		
		Outcome<Codelist> outcome = service.importCodelist(mockData,mockDirectives);
		
		Codelist list = outcome.result();
		
		//this proves that, through injection, 
		//TestParser is known to import service in association with TestDirectives 
		Assert.assertEquals(TestParser.testList,list);
	}
	
	//support
	public static class TestDirectives implements Directives<Codelist>{}

	public static class TestParser implements Parser<Codelist,TestDirectives> {
		
		static public Codelist testList = Codes.codelist().name("test").build();
		
		@Override public Class<TestDirectives> directedBy() {
			return TestDirectives.class;
		}
		
		@Override public Codelist parse(InputStream stream, TestDirectives directives) {
			return testList;
		}
		
		
	}
}
