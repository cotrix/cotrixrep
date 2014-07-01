package org.acme;

import static org.cotrix.domain.dsl.Codes.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.cotrix.common.async.ReportingFuture;
import org.cotrix.common.async.TaskObserver;
import org.cotrix.common.async.TaskUpdate;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.AsyncCodelistRepository;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class AsyncCodelistRepositoryTest extends ApplicationTest {
	
	@Inject
	private AsyncCodelistRepository repository;
	
	@Inject
	private CodelistRepository sync;

		
	@Test
	public void addCodelist() throws Exception {

		List<Code> codes = new ArrayList<Code>();
		
		for (int i =0; i<1000;i++)
			codes.add(code().name("code").build());
		
		Codelist list = codelist().name("name").with(codes).build();
	
		ReportingFuture<?> future = repository.add(list);
		
		TaskObserver<TaskUpdate> observer = new TaskObserver.Abstract<TaskUpdate>() {
			@Override
			public void on(TaskUpdate object) {
				System.out.println(object.progress()+"("+object.activity()+")");
			}
		};
	
		future.register(observer).forType(TaskUpdate.class).get(); //sync
		
		Codelist retrieved = sync.lookup(list.id());
		
		assertEquals(list,retrieved);

	}
	

	@Test
	public void removeCodelist() throws Exception {

		Definition def = definition().name("name").build();
		Attribute a = attribute().with(def).value("val").build();
		Code c = code().name("name").attributes(a).build();
		Codelist list = codelist().name("name").definitions(def).with(c).build();

		sync.add(list);

		repository.remove(list.id()).get();
		
		Codelist retrieved = sync.lookup(list.id());
		
		assertNull(retrieved);

	}
	
	@Test(expected=IllegalStateException.class)
	public void removeUnknownCodelist() throws Throwable {

		Codelist list = codelist().name("name").build();

		try {
			repository.remove(list.id()).get();
		}
		catch(ExecutionException e) {
			throw e.getCause();
		}

	}
	
	//helpers
	
//	private Codelist addAndRetrieve(Codelist list) {
//		
//		repository.add(list);
//		return repository.lookup(list.id());
//	
//	}
}
