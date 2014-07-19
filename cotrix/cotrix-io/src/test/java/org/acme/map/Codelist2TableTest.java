package org.acme.map;

import static org.acme.TestUtils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.io.tabular.map.Codelist2Table.*;
import static org.cotrix.io.tabular.map.MemberDirective.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.tabular.map.Codelist2TableDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtualrepository.tabular.Table;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class Codelist2TableTest {

	@Inject
	MapService mapper;
	
	
	Attribute a = attribute().name("a").value("v").build();
	
	Code c = code().name("c").attributes(a).build();
	
	Codelist list = codelist().name("l").with(c).build();
	
	Codelist2TableDirectives directives = new Codelist2TableDirectives();
	
	@Test
	public void defaultCode() throws Exception {

		Outcome<Table> outcome = mapper.map(list, directives);

		//System.out.println(outcome.report());
		
		assertFalse(outcome.report().logs().isEmpty());
		
		String[][] expectedData = {{"c"}};
		
		Table expected = asTable(expectedData,DEFAULT_CODECOLUMN);
		
		assertEquals(expected,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(outcome.result(),expectedData);
	}
	
	@Test
	public void customCode() throws Exception {

		directives.codeColumn("mycode");
		
		Outcome<Table> outcome = mapper.map(list, directives);

		String[][] expectedData = {{"c"}};
		
		Table expectedTable = asTable(expectedData,"mycode");
		
		assertEquals(expectedTable,outcome.result());

		assertEquals(outcome.result(),expectedData);
	}
	
	@Test
	public void defaultAttribute() throws Exception {

		directives.add(a);
		
		Outcome<Table> outcome = mapper.map(list, directives);

		String[][] expectedData = {{"c","val"}};
		
		Table expected = asTable(expectedData,DEFAULT_CODECOLUMN,"a1");
		
		assertEquals(expected,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(outcome.result(),expectedData);
	}
	
	@Test
	public void customAttribute() throws Exception {

		directives.add(map(a).to("custom"));
		
		Outcome<Table> outcome = mapper.map(list, directives);

		String[][] expectedData = {{"c","v"}};
		
		Table expected = asTable(expectedData,DEFAULT_CODECOLUMN,"custom");
		
		assertEquals(expected,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(outcome.result(),expectedData);
	}
		
}
