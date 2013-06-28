package org.cotrix.io.utils;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.io.ingest.ImportDirectives;
import org.cotrix.io.ingest.ImportTask;
import org.cotrix.io.publish.PublicationDirectives;
import org.cotrix.io.publish.PublicationTask;
import org.virtualrepository.Asset;
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
	private Instance<ImportTask<Asset,ImportDirectives>> importTasks;
	
	@Inject
	private Instance<PublicationTask<PublicationDirectives>> publishTasks;
	
	/**
	 * Produces a {@link Registry} of {@link UploadDirectives}s for CDI injection.
	 * @return the registry
	 */
	@Produces
	public Registry<ImportTask<Asset,ImportDirectives>> importTasks() {
		
		List<ImportTask<Asset,ImportDirectives>> tasks = new ArrayList<ImportTask<Asset,ImportDirectives>>();
		
		for (ImportTask<Asset,ImportDirectives> task : importTasks)
			tasks.add(task);
		
		return new Registry<ImportTask<Asset,ImportDirectives>>(tasks);
			
	}
	
	/**
	 * Produces a {@link Registry} of {@link PublicationTask}s for CDI injection.
	 * @return the registry
	 */
	@Produces 
	public Registry<PublicationTask<PublicationDirectives>> publishTasks() {
		
		List<PublicationTask<PublicationDirectives>> tasks = new ArrayList<PublicationTask<PublicationDirectives>>();
		
		for (PublicationTask<PublicationDirectives> task : publishTasks)
			tasks.add(task);
		
		return new Registry<PublicationTask<PublicationDirectives>>(tasks);
			
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
