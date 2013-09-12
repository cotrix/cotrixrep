package org.cotrix.io.utils;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.io.map.MapDirectives;
import org.cotrix.io.map.MapTask;
import org.cotrix.io.parse.ParseDirectives;
import org.cotrix.io.parse.ParseTask;
import org.cotrix.io.publish.PublicationDirectives;
import org.cotrix.io.publish.PublicationTask;
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
public class CdiProducers {

	private final static VirtualRepository repository = new Repository();
	
	@Inject
	private Instance<ParseTask<?,?>> parseTasks;
	
	@Inject
	private Instance<MapTask<?,?>> mapTasks;
	
	@Inject
	private Instance<PublicationTask<?>> publishTasks;
	
	/**
	 * Produces a {@link Registry} of {@link ParseDirectives}s for CDI injection.
	 * @return the registry
	 */
	@Produces @Singleton
	@SuppressWarnings("all")
	public Registry<ParseTask<Object,ParseDirectives<Object>>> parseTasks() {
		
		List<ParseTask<Object,ParseDirectives<Object>>> tasks = new ArrayList<ParseTask<Object,ParseDirectives<Object>>>();
		
		for (ParseTask<?,?> task : parseTasks)
			tasks.add((ParseTask)task);
		
		return new Registry<ParseTask<Object,ParseDirectives<Object>>>(tasks);
			
	}
	
	/**
	 * Produces a {@link Registry} of {@link MapDirectives}s for CDI injection.
	 * @return the registry
	 */
	@Produces @Singleton
	@SuppressWarnings("all")
	public Registry<MapTask<Object,MapDirectives<Object>>> mapTasks() {
		
		List<MapTask<Object,MapDirectives<Object>>> tasks = new ArrayList<MapTask<Object,MapDirectives<Object>>>();
		
		for (MapTask<?,?> task : mapTasks)
			tasks.add((MapTask)task);
		
		return new Registry<MapTask<Object,MapDirectives<Object>>>(tasks);
			
	}
	
	
	/**
	 * Produces a {@link Registry} of {@link PublicationTask}s for CDI injection.
	 * @return the registry
	 */
	@Produces @Singleton
	@SuppressWarnings("all")
	public Registry<PublicationTask<PublicationDirectives>> publishTasks() {
		
		List<PublicationTask<PublicationDirectives>> tasks = new ArrayList<PublicationTask<PublicationDirectives>>();
		
		for (PublicationTask<?> task : publishTasks)
			tasks.add((PublicationTask<PublicationDirectives>)task);
		
		return new Registry<PublicationTask<PublicationDirectives>>(tasks);
			
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
	 * Returns a {@link StructureParsingManager} service.
	 * 
	 * @return the service
	 */
	@Produces @Singleton
	public static StructureParsingManager parser() {
		return SdmxServiceFactory.parser();
	}
	
	/**
	 * Returns a {@link StructureWritingManager} service.
	 * 
	 * @return the service
	 */
	@Produces @Singleton
	public static StructureWritingManager writer() {
		return SdmxServiceFactory.writer();
	}
}
