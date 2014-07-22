package org.cotrix.io.impl;

import static org.cotrix.common.Report.*;
import static org.cotrix.common.CommonUtils.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.Outcome;
import org.cotrix.io.MapService;
import org.cotrix.io.utils.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link MapService}.
 * 
 * @author Fabio Simeoni
 *
 */
@Singleton
public class DefaultMapService implements MapService {

	private static Logger log = LoggerFactory.getLogger(DefaultMapService.class);
	
	private final Registry<MapTask<?,?,?>> registry;
	
	@Inject
	public DefaultMapService(Iterable<MapTask<?,?,?>> tasks) {
		
		notNull("tasks",tasks);
		
		this.registry= new Registry<MapTask<?,?,?>>(tasks);
		
		log.info("configured with tasks {}",registry.tasks());
	}
	
	@Override
	public <T,S> Outcome<S> map(T source, MapDirectives<S> directives) {
		
		notNull("source",source);
		notNull("directives",directives);
		
		double time = System.currentTimeMillis();
		
		try {
			
			//safe:tasks are indexed by their directives, convince compiler
			@SuppressWarnings("all")
			MapTask<T,S,Object> task = (MapTask) registry.get(directives);
			
			S mapped =  task.map(source,directives);
			
			time = (System.currentTimeMillis()-time)/1000;
			
			log.info("mapped source in {} secs.",time);
			
			return new Outcome<S>(mapped);
			
		}
		catch(Exception e) {
		
			throw new IllegalArgumentException("could not map source with given directives (see cause) ",e);
		}
		finally {
			
			if (report()!=null)
				report().close();

		}
	}
}
