package org.cotrix.io.impl;

import static org.cotrix.common.Utils.*;

import java.io.OutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.Report;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.utils.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link SerialisationService} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
@Singleton
public class DefaultSerialisationService implements SerialisationService {

	private static final Logger log  = LoggerFactory.getLogger(SerialisationService.class);
	
	private Registry<SerialisationTask<?,?>> registry;
	
	@Inject
	public DefaultSerialisationService(Iterable<SerialisationTask<?,?>> tasks) {
		
		notNull("serialisation tasks",tasks);
		
		this.registry=new Registry<SerialisationTask<?,?>>(tasks);
		
		log.info("configured with tasks {}",registry.tasks());
	}
	
	@Override
	public <T> void serialise(T object, OutputStream stream, SerialisationDirectives<T> directives) {
		
		notNull("target object",object);
		notNull("stream",stream);
		notNull("directives",directives);
		
		double time = System.currentTimeMillis();
		
		try {

			//safe:tasks are indexed by their directives, convince compiler
			@SuppressWarnings("all")
			SerialisationTask<Object,Object> task = (SerialisationTask) registry.get(directives);
			
			task.serialise(object,stream, directives);
			
			log.info("produced stream in {} secs.",time);
			
		}
		catch(Exception e) {
			
			throw new IllegalArgumentException("cannot produce stream with directives "+directives+" (see cause)",e);
		}
		finally {
			
			//make sure we free up resources
			Report current = Report.report();
			if (current!=null)
				current.close();
			
		}
		
		
		
	}

	

}
