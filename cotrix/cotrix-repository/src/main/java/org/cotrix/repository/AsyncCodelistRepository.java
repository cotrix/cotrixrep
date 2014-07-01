package org.cotrix.repository;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.cotrix.common.async.ExecutionService;
import org.cotrix.common.async.ReportingFuture;
import org.cotrix.domain.codelist.Codelist;

public class AsyncCodelistRepository {

	@Inject
	private CodelistRepository inner;
	
	@Inject
	private ExecutionService service;
	
	public ReportingFuture<?> add(final Codelist codelist) {
		
		return service.execute(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				inner.add(codelist);
				return null;
			}
		});
	}
	
	public ReportingFuture<?> remove(final String id) {
		return service.execute(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				inner.remove(id);
				return null;
			}
		});
	}
}
