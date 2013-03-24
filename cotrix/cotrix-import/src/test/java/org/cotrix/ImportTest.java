package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.cotrix.domain.Codelist;
import org.cotrix.importservice.DefaultImportService;
import org.cotrix.importservice.Directives;
import org.cotrix.importservice.ImportService;
import org.cotrix.importservice.Parser;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.memory.MCodebagRepository;
import org.cotrix.repository.memory.MCodelistRepository;
import org.junit.Test;

public class ImportTest {

	@Test
	public void importSimpleCodelist() throws Exception {

		class TestDirectives implements Directives<Codelist> {}
		Codelist importedList = codelist().name("name").build();
		
		@SuppressWarnings("all")
		Parser<Codelist, TestDirectives> mockParser = mock(Parser.class);
		when(mockParser.directedBy()).thenReturn(TestDirectives.class);
		when(mockParser.parse(any(InputStream.class), any(TestDirectives.class))).thenReturn(importedList);
		
		Map<Class<?>,Parser<?,?>> registry = new HashMap<Class<?>, Parser<?,?>>();
		registry.put(TestDirectives.class,mockParser);
		
		CodelistRepository repository = new MCodelistRepository();
		
		ImportService service = new DefaultImportService(registry,repository, new MCodebagRepository());
		
		InputStream mockData = mock(InputStream.class);
		
		Codelist list = service.importCodelist(mockData, new TestDirectives());

		//we just confirm the list has been stored
		//(we test mappers for the correctness of mappings)
		assertNotNull(list.id());
		assertEquals(importedList.name(),list.name());

	}
}
