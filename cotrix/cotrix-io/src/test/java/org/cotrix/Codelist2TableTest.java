package org.cotrix;

import static org.cotrix.TestUtils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.io.tabular.map.AttributeDirectives.*;
import static org.cotrix.io.tabular.map.Codelist2Table.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
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
	
	@Test
	public void defaultCodeColumn() throws Exception {

		Code code = code().name("c").build();
		Codelist list = codelist().name("list").with(code).build();
		
		Codelist2TableDirectives directives = new Codelist2TableDirectives();
		
		Outcome<Table> outcome = mapper.map(list, directives);

		System.out.println(outcome.report());
		
		assertFalse(outcome.report().logs().isEmpty());
		
		String[][] expectedData = {{"c"}};
		
		Table expected = asTable(expectedData,DEFAULT_CODE_COLUMN_NAME);
		
		assertEquals(expected,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(outcome.result(),expectedData);
	}
	
	@Test
	public void customCodeColumn() throws Exception {

		Code code = code().name("c").build();
		Codelist list = codelist().name("list").with(code).build();
		
		Codelist2TableDirectives directives = new Codelist2TableDirectives();
		directives.codeColumnName("mycode");
		
		Outcome<Table> outcome = mapper.map(list, directives);

		System.out.println(outcome.report());
		
		String[][] expectedData = {{"c"}};
		
		Table expected = asTable(expectedData,"mycode");
		
		assertEquals(expected,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(outcome.result(),expectedData);
	}
	
	@Test
	public void attributeSelectedByName() throws Exception {

		Attribute a = attr().name("a1").value("val").build();
		
		Code code = code().name("c").attributes(a).build();
		Codelist list = codelist().name("list").with(code).build();
		
		Codelist2TableDirectives directives = new Codelist2TableDirectives();
		directives.codeColumnName("mycode");
		
		Attribute template = attr().name("a1").build();
		directives.add(template);
		
		Outcome<Table> outcome = mapper.map(list, directives);

		System.out.println(outcome.report());
		
		String[][] expectedData = {{"c","val"}};
		
		Table expected = asTable(expectedData,"mycode","a1");
		
		assertEquals(expected,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(outcome.result(),expectedData);
	}
	
	@Test
	public void attributeSelectedByNameWithCustomisation() throws Exception {

		Attribute a = attr().name("a1").value("val").build();
		
		Code code = code().name("c").attributes(a).build();
		Codelist list = codelist().name("list").with(code).build();
		
		Codelist2TableDirectives directives = new Codelist2TableDirectives();
		directives.codeColumnName("mycode");
		
		Attribute template = attr().name("a1").build();
		
		directives.add(map(template).to("custom"));
		
		Outcome<Table> outcome = mapper.map(list, directives);

		System.out.println(outcome.report());
		
		String[][] expectedData = {{"c","val"}};
		
		Table expected = asTable(expectedData,"mycode","custom");
		
		assertEquals(expected,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(outcome.result(),expectedData);
	}
	
	
	@Test
	public void attributeSelectedByNameAndType() throws Exception {

		Attribute a1 = attr().name("a1").value("val1").build();
		Attribute a2 = attr().name("a2").value("val2").ofType("type").build();
		
		Code code = code().name("c").attributes(a1,a2).build();
		Codelist list = codelist().name("list").with(code).build();
		
		Codelist2TableDirectives directives = new Codelist2TableDirectives();
		directives.codeColumnName("mycode");
		
		Attribute template = attr().name("a2").value("..").ofType("type").build();
		
		directives.add(map(template).to("a2-type"));
		
		Outcome<Table> outcome = mapper.map(list, directives);

		System.out.println(outcome.report());
		
		String[][] expectedData = {{"c","val2"}};
		
		Table expected = asTable(expectedData,"mycode","a2-type");
		
		assertEquals(expected,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(outcome.result(),expectedData);
	}
	
	@Test
	public void attributeSelectedByNameAndLanguage() throws Exception {

		Attribute a1 = attr().name("a1").value("val1").in("en").build();
		Attribute a2 = attr().name("a1").value("val2").in("fr").build();
		
		Code code = code().name("c").attributes(a1,a2).build();
		Codelist list = codelist().name("list").with(code).build();
		
		Codelist2TableDirectives directives = new Codelist2TableDirectives();
		directives.codeColumnName("mycode");
		
		Attribute template = attr().name("a1").value("..").in("fr").build();
		
		directives.add(map(template).to("a1-fr"));
		
		Outcome<Table> outcome = mapper.map(list, directives);

		System.out.println(outcome.report());
		
		String[][] expectedData = {{"c","val2"}};
		
		Table expected = asTable(expectedData,"mycode","a1-fr");
		
		assertEquals(expected,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(outcome.result(),expectedData);
	}
	
}
