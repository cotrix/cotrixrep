package org.acme.domain;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.neo.domain.NeoAttribute;
import org.cotrix.neo.domain.NeoCode;
import org.cotrix.neo.domain.NeoCodelist;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class MappingTest extends ApplicationTest {

	@Inject
	GraphDatabaseService store;
	
	@Test
	public void attributesRoundTrip() {
		
		Attribute a = attribute().name("a").value("v").ofType("t").in("l").build();
		
		Attribute.State created = reveal(a).state();
		
		try (Transaction tx = store.beginTx()) {
				
			NeoAttribute added = new NeoAttribute(created);
			
			NeoAttribute retrieved = new NeoAttribute(added.node());
			
			assertEquals(created,retrieved);
			
			tx.success();
		}
		
	}
	
	@Test
	public void codesRoundTrip() {
		
		Attribute a = attribute().name("a").value("v").ofType("t").in("l").build();
		Code c = code().name("n").attributes(a).build();
		
		Code.State created = reveal(c).state();
		
		try (Transaction tx = store.beginTx()) {
			
			NeoCode added = new NeoCode(created);
			
			NeoCode retrieved = new NeoCode(added.node());
			
			assertEquals(created.name(),retrieved.name());
			
			assertEquals(collect(created.attributes()),collect(retrieved.attributes()));
			
			assertEquals(created.attributes().size(),retrieved.attributes().size());
			
			assertTrue(retrieved.attributes().contains(a.id()));
			
			
			assertTrue(retrieved.attributes().contains(reveal(a).state()));
			assertTrue(retrieved.attributes().contains(a.name()));
				
			
			retrieved.attributes().remove(a.id());
			assertFalse(retrieved.attributes().contains(a.id()));
			
			tx.success();
		}
		
	}
	
	@Test
	public void codelistsRoundTrip() {
		
		Attribute a = attribute().name("a").value("v").ofType("t").in("l").build();
		Code c = code().name("n").attributes(a).build();
		Codelist cl = codelist().name("n").with(c).version("1.3").build();
		
		Codelist.State created = reveal(cl).state();
		
		try (Transaction tx = store.beginTx()) {
			
			NeoCodelist added = new NeoCodelist(created);
			
			NeoCodelist retrieved = new NeoCodelist(added.node());
			
			assertEquals(created.name(),retrieved.name());
			assertEquals(collect(created.attributes()),collect(retrieved.attributes()));
			assertEquals(collect(created.links()),collect(retrieved.links()));
			assertEquals(created.version(),retrieved.version());
			
			tx.success();
		}
		
	}
}
