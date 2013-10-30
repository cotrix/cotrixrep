package org.cotrix;

import static junit.framework.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.repository.Queries.*;

import java.util.Arrays;
import java.util.Iterator;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.utils.UuidGenerator;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.memory.MCodelistRepository;
import org.cotrix.repository.query.CodelistQuery;
import org.cotrix.repository.query.Range;
import org.junit.Test;

public class MemoryQueryTest {

	
	@Test
	public void allCodelists() {
		
		CodelistRepository repository = new MCodelistRepository(new UuidGenerator());
		
		Codelist list = codelist().name("name").build();
		
		repository.add(list);
		
		Iterator<Codelist> lists  = repository.queryFor(allLists()).iterator();
		
		assertEquals(list.name(),lists.next().name());
		assertFalse(lists.hasNext());
	}
	
	@Test
	public void codeRanges() {
		
		CodelistRepository repository = new MCodelistRepository(new UuidGenerator());
		
		Code code1 = code().name("c1").build();
		Code code2 = code().name("c2").build();
		Code code3 = code().name("c3").build();
		Codelist list = codelist().name("l").with(code1,code2,code3).build();
		
		repository.add(list);
		
		System.out.println(list);
		
		CodelistQuery<Code> codes = allCodes(list.id());
		
		codes.setRange(new Range(2,3));
		
		Iterable<Code> inrange  = repository.queryFor(codes);
		
		assertEquals(Arrays.asList(code2,code3),inrange);
	}
}
