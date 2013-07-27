package org.cotrix.io.parse;

import static org.cotrix.domain.utils.Utils.*;

import java.io.InputStream;

import javax.inject.Inject;

import org.cotrix.io.utils.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultParseService implements ParseService {

	private static Logger log = LoggerFactory.getLogger(ParseService.class);
	
	private final Registry<ParseTask<Object,ParseDirectives<Object>>> registry;
	
	@Inject
	public DefaultParseService(Registry<ParseTask<Object,ParseDirectives<Object>>> registry) {
		
		notNull("parser registry",registry);
		
		this.registry=registry;
		
		log.info("configured with tasks {}",registry.getAll());
	}
	
	@Override
	public <T> T parse(InputStream stream, ParseDirectives<T> directives) {
		
		notNull("stream",stream);
		notNull("parse directives",directives);
		
		double time = System.currentTimeMillis();
		
		try {

			//safe: parse directives are always bound to parse tasks, convince compiler
			@SuppressWarnings("all")
			ParseTask<T,ParseDirectives<T>> task = (ParseTask) registry.get(directives);
			
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
