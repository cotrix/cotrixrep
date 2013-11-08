package org.cotrix.io.utils;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.io.impl.MapTask;
import org.cotrix.io.impl.ParseTask;
import org.cotrix.io.impl.SerialisationTask;
import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.impl.Repository;

/**
 * Produces objects for CDI injection.
 * 
 * @author Fabio Simeoni
 *
 */
@SuppressWarnings("all")
public class CdiProducers {

	private final static VirtualRepository repository = new Repository();
	
	@Inject
	private Instance<ParseTask<?,?>> parseTasks;
	
	@Inject
	private Instance<MapTask<?,?,?>> mapTasks;
	
	@Inject
	private Instance<SerialisationTask<?,?>> serialisationTasks;
	

	@Produces
	public Iterable<ParseTask<Object,Object>> parseTasks() {
		return (Iterable) parseTasks;
	}
	

	@Produces
	public Iterable<MapTask<Object,Object,Object>> mapTasks() {
		return (Iterable) mapTasks;		
	}
	
	
	@Produces
	public Iterable<SerialisationTask<Object,Object>> publishTasks() {
		return (Iterable) serialisationTasks;
	}
	
	/**
	 * Makes available a {@link VirtualRepository} for injection.
	 * @return the repository
	 */
	@Produces @Singleton
	public static VirtualRepository virtualRepository() {
		return repository;
	}

	
	/**
	 * Returns a {@link StructureParsingManager} serialiser.
	 * 
	 * @return the serialiser
	 */
	@Produces @Singleton
	public static StructureParsingManager parser() {
		return SdmxServiceFactory.parser();
	}
	
	/**
	 * Returns a {@link StructureWritingManager} serialiser.
	 * 
	 * @return the serialiser
	 */
	@Produces @Singleton
	public static StructureWritingManager writer() {
		return SdmxServiceFactory.writer();
	}
}
