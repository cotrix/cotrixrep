package org.cotrix.io.impl;

import static org.cotrix.common.Utils.*;

import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.io.ParseService;
import org.cotrix.io.utils.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class DefaultParseService implements ParseService {

	private static Logger log = LoggerFactory.getLogger(ParseService.class);
	
	private final Registry<ParseTask<?,?>> registry;
	
	@Inject
	public DefaultParseService(Iterable<ParseTask<?,?>> tasks) {
		
		notNull("parse tasks",tasks);
		
		this.registry=new Registry<ParseTask<?,?>>(tasks);
		
		log.info("configured with tasks {}",registry.tasks());
	}
	
	@Override
	public <T> T parse(InputStream stream, ParseDirectives<T> directives) {
		
		notNull("stream",stream);
		notNull("directives",directives);
		
		double time = System.currentTimeMillis();
		
		try {

			//safe:tasks are indexed by their directives, convince compiler
			@SuppressWarnings("all")
			ParseTask<T,Object> task = (ParseTask) registry.get(directives);
			
			T result =  task.parse(stream,directives);
			
			time = (System.currentTimeMillis()-time)/1000;
			
			log.info("parsed stream in {} secs.",time);
			
			return result;
			
		}
		catch(Exception e) {
			throw new IllegalStateException("could not parse stream with directives "+directives+" (see cause) ",e);
		}
	}
}
