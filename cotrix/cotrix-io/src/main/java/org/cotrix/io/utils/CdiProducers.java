package org.cotrix.io.utils;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.io.ingest.ImportTask;
import org.cotrix.io.publish.PublicationTask;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;

/**
 * Produces objects for CDI injection.
 * 
 * @author Fabio Simeoni
 *
 */
public class CdiProducers {

	@Inject
	private Instance<ImportTask<?,?>> importTasks;
	
	@Inject
	private Instance<PublicationTask<?>> publishTasks;
	
	/**
	 * Produces a {@link Registry} of {@link UploadDirectives}s for CDI injection.
	 * @return the registry
	 */
	@Produces
	public Registry<ImportTask<?,?>> importTasks() {
		
		List<ImportTask<?,?>> tasks = new ArrayList<ImportTask<?,?>>();
		
		for (ImportTask<?,?> task : importTasks)
			tasks.add(task);
		
		return new Registry<ImportTask<?,?>>(tasks);
			
	}
	
	/**
	 * Produces a {@link Registry} of {@link PublicationTask}s for CDI injection.
	 * @return the registry
	 */
	@Produces 
	public Registry<PublicationTask<?>> publishTasks() {
		
		List<PublicationTask<?>> tasks = new ArrayList<PublicationTask<?>>();
		
		for (PublicationTask<?> task : publishTasks)
			tasks.add(task);
		
		return new Registry<PublicationTask<?>>(tasks);
			
	}
	
	/**
	 * Makes available a {@link VirtualRepository} for injection.
	 * @return the repository
	 */
	@Produces @Singleton
	public VirtualRepository virtualRepository() {
		return new Repository();
	}
}
