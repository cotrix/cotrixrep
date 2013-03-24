package org.cotrix.importservice.utils;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.importservice.DefaultImportService;
import org.cotrix.importservice.Parser;

/**
 * Produces the configuration for a {@link DefaultImportService} in a CDI container.
 * 
 * @author Fabio Simeoni
 *
 */
public class RegistryProducer {

	@Inject
	private Instance<Parser<?,?>> parsers;
	
	/**
	 * Makes available the configuration of a {@link DefaultImportService} for injection.
	 * @return the configuration
	 */
	@Produces @ParserRegistry
	public Map<Class<?>,Parser<?,?>> registry() {
		
		Map<Class<?>,Parser<?,?>> registry = new HashMap<Class<?>,Parser<?,?>>();
		
		//iterates over all discovered parsers to prepare the configuration
		//note: type parameters on Parsers ensure that directives and parsers match
		for (Parser<?,?> parser : parsers)
			registry.put(parser.directedBy(),parser);
		
		return registry;
			
	}
}
