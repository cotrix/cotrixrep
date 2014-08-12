package org.acme.map;

import static org.acme.TestUtils.*;
import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.io.tabular.map.ColumnDirectives.*;
import static org.cotrix.io.tabular.map.MappingMode.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.tabular.map.Table2CodelistDirectives;
import org.cotrix.test.DomainTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtualrepository.tabular.Table;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class Table2CodelistTest extends DomainTest {

	@Inject
	MapService mapper;

	@Test
	public void defaultNameIsCodeColumn() {
		
		String[][] data = {{}};

		Table table = asTable(data,"c1");

		Table2CodelistDirectives directives = new Table2CodelistDirectives("c1");

		directives.mode(ignore);

		Outcome<Codelist> outcome = mapper.map(table, directives);

		System.out.println(outcome.report());

		Codelist expected = codelist().name("c1").build();

		assertEquals(expected, outcome.result());

	}
	
	@Test
	public void nameCanBeCustomised() {
		
		String[][] data = {{}};

		Table table = asTable(data,"c1");

		Table2CodelistDirectives directives = new Table2CodelistDirectives("c1");

		directives.mode(ignore);
		directives.name("custom");

		Outcome<Codelist> outcome = mapper.map(table, directives);

		System.out.println(outcome.report());

		Codelist expected = codelist().name("custom").build();

		assertEquals(expected, outcome.result());

	}
	
	@Test
	public void versionCanBeCustomised() {
		
		String[][] data = {{}};

		Table table = asTable(data,"c1");

		Table2CodelistDirectives directives = new Table2CodelistDirectives("c1");

		directives.mode(ignore);
		directives.version("2");

		Outcome<Codelist> outcome = mapper.map(table, directives);

		System.out.println(outcome.report());

		Codelist expected = codelist().name("c1").version("2").build();

		assertEquals(expected, outcome.result());

	}
	
	@Test
	public void codeNameIsCodeColumnValue() {
		
		String[][] data = {{"1"}};

		Table table = asTable(data,"c1");

		Table2CodelistDirectives directives = new Table2CodelistDirectives("c1");

		Outcome<Codelist> outcome = mapper.map(table, directives);

		System.out.println(outcome.report());

		Codelist expected = codelist().name("c1").with(code().name("1").build()).build();

		assertEquals(expected, outcome.result());
	}
	
	@Test
	public void codeAttributeIsMappedColumnValue() {
		
		String[][] data = {{"1", "2"}};

		Table table = asTable(data,"c1","c2");

		Table2CodelistDirectives directives = new Table2CodelistDirectives("c1");

		directives.add(column("c2").language("l").type("t"));

		Outcome<Codelist> outcome = mapper.map(table, directives);

		System.out.println(outcome.report());

		Attribute attr = attribute().name("c2").value("2").ofType("t").in("l").build();
		
		Code code = code().name("1").attributes(attr).build();
		
		Codelist expected = codelist().name("c1").with(code).build();

		assertEquals(expected, outcome.result());
	}
	
	@Test
	public void attributeDefinitionAreShared() {
		
		String[][] data = {{"1", "2"}, { "3", "4" }};

		Table table = asTable(data,"c1","c2");

		Table2CodelistDirectives directives = new Table2CodelistDirectives("c1");

		directives.add(column("c2").name("n").language("en").type("t"));
		
		Outcome<Codelist> outcome = mapper.map(table, directives);

		Codelist list = outcome.result();

		assertEquals(1,list.attributeDefinitions().size());
		
		AttributeDefinition def = list.attributeDefinitions().iterator().next();
		
		Code code1 = list.codes().getFirst(q("1"));
		Code code2 = list.codes().getFirst(q("3"));
		
		Attribute attr1 = code1.attributes().getFirst(q("n"));
		Attribute attr2 = code2.attributes().getFirst(q("n"));
		
		assertEquals(attr1.definition(), attr2.definition());
		assertEquals(def, attr2.definition());
	}


}