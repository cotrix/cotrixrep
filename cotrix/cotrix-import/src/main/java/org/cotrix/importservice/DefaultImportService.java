package org.cotrix.importservice;

import static org.cotrix.domain.utils.Utils.*;

import java.io.InputStream;
import java.util.Map;

import javax.inject.Inject;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.trait.Named;
import org.cotrix.importservice.utils.ParserRegistry;
import org.cotrix.repository.CodebagRepository;
import org.cotrix.repository.CodelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link ImportService}.
 * 
 * @author Fabio Simeoni
 *
 */
public class DefaultImportService implements ImportService {
	
	private static Logger log = LoggerFactory.getLogger(DefaultImportService.class);
	
	private final Map<Class<?>,Parser<?,?>> parsers;
	private final CodelistRepository listRepository;
	private final CodebagRepository bagRepository;
	
	@Inject
	public DefaultImportService(@ParserRegistry Map<Class<?>,Parser<?,?>> parsers, 
												CodelistRepository listRepository,
												CodebagRepository bagRepository) {
		
		notNull("parser registry",parsers);
		notNull("listRepository",listRepository);
		notNull("codelist repository",listRepository);
		notNull("codebag repository",listRepository);
		
		this.parsers=parsers;
		this.listRepository = listRepository;
		this.bagRepository=bagRepository;
		
		log.info("import service is configured with parsers {}",parsers.values());
	}
	
	@Override
	public Outcome<Codelist> importCodelist(InputStream data, Directives<Codelist> directives) {
		
		double time = System.currentTimeMillis();
		
		Outcome<Codelist> outcome = _import(data,directives);
		
		try {
			
			Codelist imported = outcome.result();
		
			listRepository.add(imported);
		
			time = (System.currentTimeMillis()-time)/1000;
		
			log.info("imported codelist '{}' with {} codes in {} secs.",imported.name(),imported.codes().size(),time);
		}
		catch(ImportFailureException e) {
			
			log.info("failed importing codelist:\n{}",outcome.report());
		}
		
		return outcome;
	}
	
	@Override
	public Outcome<Codebag> importCodebag(InputStream data, Directives<Codebag> directives) {		
		
		double time = System.currentTimeMillis();
		
		Outcome<Codebag> outcome = _import(data,directives);
		
		try {
			
			Codebag imported = outcome.result();
			
			bagRepository.add(imported);
			
			time = (System.currentTimeMillis()-time)/1000;
			
			log.info("imported codebag '{}' with {} lists in {} secs",imported.name(),imported.lists().size(), time);
		}
		catch(ImportFailureException e) {
			
			log.trace("failed importing codebag:\n{}",outcome.report());
			
		}
		
		return outcome;
	};
	
	private <T extends Named> Outcome<T> _import(InputStream data, Directives<T> directives)  {
		
		notNull("data",data);
		notNull("import directives",directives);
		
		try {
			
			@SuppressWarnings("all")
			Parser<T,Directives<T>> parser = (Parser) parsers.get(directives.getClass());
			
			if (parser==null)
				throw new IllegalStateException("configuration error: no parser for directives "+directives);
			
			T parsed = parser.parse(data,directives);
			
			return new Outcome<T>(parsed);
			
		}
		catch(ClassCastException e) {
			throw new IllegalStateException("configuration error: parser does not match directives "+directives);
		}
	};
}
