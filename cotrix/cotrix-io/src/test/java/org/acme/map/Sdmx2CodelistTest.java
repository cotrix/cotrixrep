package org.acme.map;

import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.io.sdmx.SdmxElement.*;
import static org.cotrix.io.sdmx.map.Sdmx2CodelistDirectives.*;
import static org.junit.Assert.*;
import static org.sdmx.CodelistBuilder.*;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.test.DomainTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmx.CodelistBuilder;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class Sdmx2CodelistTest extends DomainTest {

	@Inject
	MapService mapper;
	
	CodelistBuilder start = list();

	@Test
	public void definitionsAreShared() {
		
		CodelistBean  bean = start.add(code("c1").name("n1", "en"))
								  .add(code("c2").name("n2", "en"))
								  .add(code("c3").name("n3", "es"))
							 .end();
		
		Outcome<Codelist> outcome = mapper.map(bean,DEFAULT);
		
		Codelist codelist = outcome.result();
		
		assertEquals(2,codelist.attributeDefinitions().size());
		
		Code c1 = codelist.codes().getFirst(q("c1"));
		Code c2 = codelist.codes().getFirst(q("c2"));
		Code c3 = codelist.codes().getFirst(q("c3"));
		
		AttributeDefinition d1 = c1.attributes().getFirst(DEFAULT.get(NAME)).definition();
		AttributeDefinition d2 = c2.attributes().getFirst(DEFAULT.get(NAME)).definition();
		AttributeDefinition d3 = c3.attributes().getFirst(DEFAULT.get(NAME)).definition();
		
		assertEquals(d1,d2);
		assertNotEquals(d1,d3);
	}


}