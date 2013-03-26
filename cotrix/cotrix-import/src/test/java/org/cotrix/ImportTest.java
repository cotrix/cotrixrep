package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.cotrix.domain.Codelist;
import org.cotrix.importservice.DefaultImportService;
import org.cotrix.importservice.Directives;
import org.cotrix.importservice.ImportFailureException;
import org.cotrix.importservice.ImportService;
import org.cotrix.importservice.Outcome;
import org.cotrix.importservice.Parser;
import org.cotrix.importservice.Report;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.memory.MCodebagRepository;
import org.cotrix.repository.memory.MCodelistRepository;
import org.junit.Test;

public class ImportTest {

	class TestDirectives implements Directives<Codelist> {}
	
	abstract class MockParser implements Parser<Codelist,TestDirectives> {
	
		@Override 
		public Class<TestDirectives> directedBy() {
			return TestDirectives.class;
		}
	}
	
	Codelist testList = codelist().name("testlist").build();
	TestDirectives directives = new TestDirectives();
	ImportService service;
	CodelistRepository repository=new MCodelistRepository();
	InputStream mockStream = new ByteArrayInputStream(new byte[0]);
	
	void configureImportServiceWith(MockParser parser) {
		
		Map<Class<?>,Parser<?,?>> registry = new HashMap<Class<?>, Parser<?,?>>();
		registry.put(TestDirectives.class,parser);
		
		service = new DefaultImportService(registry,repository, new MCodebagRepository());
	}
	
	@Test
	public void importSimpleCodelist() throws Exception {
		
		@SuppressWarnings("all")
		MockParser parser = new MockParser() {
			
			@Override
			public Codelist parse(InputStream stream, TestDirectives directives) {
				Report.report().log("all good");
				return testList;
			}
		};
		
		configureImportServiceWith(parser);
		
		Outcome<Codelist> outcome = service.importCodelist(mockStream,directives);

		assertNotNull(outcome.report());
		
		Codelist list = outcome.result();
				
		//we just confirm the list has been stored
		//(we test mappers for the correctness of mappings)
		assertNotNull(list.id());
		assertEquals(testList.name(),list.name());
	}
	
	@Test
	public void importFailuresAreReported() throws Exception {
		
		@SuppressWarnings("all")
		MockParser parser = new MockParser() {
			
			@Override
			public Codelist parse(InputStream stream, TestDirectives directives) {
				Report.report().logError("oopsie");
				return testList;
			}
		};
		
		configureImportServiceWith(parser);
		
		Outcome<Codelist> outcome = service.importCodelist(mockStream,directives);

		assertNotNull(outcome.report());
		
		try {
			outcome.result();
			fail();
		}
		catch(ImportFailureException e) {
			
			System.out.println(e.getMessage());
		}
		
	}
}
