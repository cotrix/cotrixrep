package org.cotrix.repository.utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.repository.Queries;
import org.cotrix.repository.QueryFactory;

/**
 * Extends a DI container to inject a {@link QueryFactory} in {@link Queries}
 * 
 * @author Fabio Simeoni
 *
 */
//Queries is static for user convenience, this extends the DI container to custom-inject into it.
@Singleton
public class QueryFactoryInjector {

	@Inject 
	private QueryFactory factory;
	
	@PostConstruct
	void startup() {		
		Queries.setFactory(factory);
	}
}


