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
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.LinkDefinition;
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
	
	
	//---- codelist fixture
	
		Attribute ta = attribute().name("ta").value("tv").build();
		Code tc = code().name("tc").attributes(ta).build();
		Codelist tlist = codelist().name("t").with(tc).build();
	
	LinkDefinition nl = listLink().name("nl").target(tlist).build();
	LinkDefinition al = listLink().name("al").target(tlist).anchorTo(ta).build();
	
	Attribute a = attribute().name("a").value("v").build();
	Codelink l1 = link().instanceOf(nl).target(tc).build();
	Codelink l2 = link().instanceOf(al).target(tc).build();
	
	Code c = code().name("c").attributes(a).links(l1,l2).build();
	
	
	Codelist list = codelist().name("l").links(nl,al).with(c).build();
	
	//---------------------------------------------------------------------
	
	Codelist2TableDirectives directives = new Codelist2TableDirectives();
	
	@Test
	public void defaultCode() throws Exception {

		Outcome<Table> outcome = mapper.map(list, directives);

		//System.out.println(outcome.report());
		
		assertFalse(outcome.report().logs().isEmpty());
		
		String[][] expectedData = {{"c"}};
		
		Table expectedTable = asTable(expectedData,DEFAULT_CODECOLUMN);
		
		assertEquals(expectedTable,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(expectedData,outcome.result());
	}
	
	@Test
	public void customCode() throws Exception {

		directives.codeColumn("mycode");
		
		Outcome<Table> outcome = mapper.map(list, directives);

		String[][] expectedData = {{"c"}};
		
		Table expectedTable = asTable(expectedData,"mycode");
		
		assertEquals(expectedTable,outcome.result());

		assertEquals(expectedData,outcome.result());
	}
	
	@Test
	public void defaultAttribute() throws Exception {

		directives.add(a);
		
		Outcome<Table> outcome = mapper.map(list, directives);

		String[][] expectedData = {{"c","v"}};
		
		Table expectedTable = asTable(expectedData,DEFAULT_CODECOLUMN,"a");
		
		assertEquals(expectedTable,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(expectedData,outcome.result());
	}
	
	@Test
	public void customAttribute() throws Exception {

		directives.add(map(a).to("custom"));
		
		Outcome<Table> outcome = mapper.map(list, directives);

		String[][] expectedData = {{"c","v"}};
		
		Table expectedTable = asTable(expectedData,DEFAULT_CODECOLUMN,"custom");
		
		assertEquals(expectedTable,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(expectedData,outcome.result());
	}
		
	@Test
	public void defaultNameLink() throws Exception {

		directives.add(nl);
		
		Outcome<Table> outcome = mapper.map(list, directives);

		String[][] expectedData = {{"c","tc"}};
		
		Table expectedTable = asTable(expectedData,DEFAULT_CODECOLUMN,"nl");
		
		assertEquals(expectedTable,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(expectedData,outcome.result());
	}
	
	@Test
	public void defaultAttributeLink() throws Exception {
		
		directives.add(al);
		
		Outcome<Table> outcome = mapper.map(list, directives);

		String[][] expectedData = {{"c","tv"}};
		
		Table expectedTable = asTable(expectedData,DEFAULT_CODECOLUMN,"al");
		
		assertEquals(expectedTable,outcome.result());
		
		//System.out.println(serialise(outcome.result()));

		assertEquals(expectedData,outcome.result());
	}
}
