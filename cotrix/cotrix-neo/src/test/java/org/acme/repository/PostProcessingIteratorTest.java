package org.acme.repository;

import static java.util.Arrays.*;
import static java.util.Calendar.*;
import static java.util.concurrent.TimeUnit.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.repository.CodelistQueries.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.neo.repository.PostProcessingIterator;
import org.cotrix.neo.repository.PostProcessingIterator.PostProcessor;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.Range;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class PostProcessingIteratorTest extends ApplicationTest {

	@Inject
	CodelistRepository codelists;
	
	@Test
	public void iterationWrapperRespectsIteratorSemantics() {
		
		List<Integer> ints = asList(1,2,3);
		
		PostProcessor<Integer> processor = new PostProcessor<Integer>() {
			public boolean match(Integer result) {
				return result>=2;
			};
		};
		
		Collection<Integer> processed;
		
		processed = collect(new PostProcessingIterator<Integer>(ints.iterator(),new Range(1,1), processor));
		
		assertEquals(asList(2),processed);
		
		processed = collect(new PostProcessingIterator<Integer>(ints.iterator(),new Range(1,10), processor));
		
		assertEquals(asList(2,3),processed);
		
		processed = collect(new PostProcessingIterator<Integer>(ints.iterator(),new Range(2,10), processor));
		
		assertEquals(asList(3),processed);
		
		processed = collect(new PostProcessingIterator<Integer>(ints.iterator(),new Range(5,10), processor));
		
		assertEquals(asList(),processed);
		
		processor = new PostProcessor<Integer>() {
			public boolean match(Integer result) {
				return false;
			};
		};
		
		processed = collect(new PostProcessingIterator<Integer>(ints.iterator(),new Range(1,10), processor));
		
		assertEquals(asList(),processed);

	}
	
	@Test
	public void codesChangedSince() throws Exception {

		
		
		Code c1 = code().name("c1").build();
		CREATED.set(time()).on(c1);
		
		Code c2 = code().name("c2").build();
		CREATED.set(time()).on(c2);
		
		Codelist list = codelist().name("name").with(c1,c2).build();
		
		codelists.add(list);
		
		Date checkpoint = getInstance().getTime();
		
		SECONDS.sleep(1);

		Code c3 = code().name("c3").build();
		CREATED.set(time()).on(c3);

		Code c1mod = modify(c1).name("newname").build();
		LAST_UPDATED.set(time()).on(c1mod);
		
		Codelist changeset = modify(list).with(c3,c1mod).build();
		
		codelists.update(changeset);
		
		Iterable<Code> codes  = codelists.get(codesSince(checkpoint).in(list.id()).from(1).to(100));
		
		assertEquals(collect(codes),asList(c1,c3));
		
	}
}
