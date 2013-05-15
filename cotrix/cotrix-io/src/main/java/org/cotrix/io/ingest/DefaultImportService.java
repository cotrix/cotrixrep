package org.cotrix.io.ingest;

import static org.cotrix.domain.utils.Utils.*;

import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.domain.Codelist;
import org.cotrix.io.Channels;
import org.cotrix.io.ImportService;
import org.cotrix.io.PublicationService;
import org.cotrix.io.utils.Registry;
import org.cotrix.repository.CodelistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.Asset;
import org.virtualrepository.VirtualRepository;

/**
 * Default implementation of {@link PublicationService}.
 * 
 * @author Fabio Simeoni
 *
 */
@Singleton
public class DefaultImportService implements ImportService {
	
	private static Logger log = LoggerFactory.getLogger(ImportService.class);
	
	private final Registry<ImportTask<?,?>> registry;
	private final CodelistRepository listRepository;
	private final VirtualRepository repository;
	
	@Inject
	public DefaultImportService(Registry<ImportTask<?,?>> registry,CodelistRepository listRepository, VirtualRepository repository) {
		
		notNull("parser registry",registry);
		notNull("codelist repository",listRepository);
		notNull("virtual repository",listRepository);
		
		this.registry=registry;
		this.listRepository = listRepository;
		this.repository = repository;
		
		log.info("configured with tasks {}",registry.getAll());
	}
	
	@Override
	public Iterable<Asset> remoteCodelists() {
		return repository;
	}
	
	@Override
	public int discoverRemoteCodelists() {
		return repository.discover(Channels.importTypes);
	}
	
	@Override
	public int discoverRemoteCodelists(int timeout) {
		return repository.discover(timeout,Channels.importTypes);
	}
	
	@Override
	public Outcome<Codelist> importCodelist(InputStream data, ImportDirectives directives) {
		
		notNull("data",data);
		notNull("upload mockDirectives",directives);
		
		double time = System.currentTimeMillis();
		
		Outcome<Codelist> outcome =  null;
		
		try {
			
			//safe: upload mockDirectives are always bound to upload tasks, convince compiler
			@SuppressWarnings("all")
			ImportTask<?,ImportDirectives> task = (ImportTask) registry.get(directives);
			
			Codelist parsed = task.parse(data,directives);
			
			outcome = new Outcome<Codelist>(parsed);
			
		}
		catch(Exception e) {
			throw new IllegalStateException("could not upload data stream with mockDirectives "+directives+" (see cause) ",e);
		}

		try {
			
			Codelist imported = outcome.result();
		
			listRepository.add(imported);
		
			time = (System.currentTimeMillis()-time)/1000;
		
			log.info("imported codelist '{}' with {} codes in {} secs.",imported.name(),imported.codes().size(),time);
		}
		catch(Exception e) {
			
			log.info("failed importing codelist:\n{}",outcome.report());
		}
		
		return outcome;
	}
	
	@Override
	public Outcome<Codelist> importCodelist(String assetId, ImportDirectives directives) {
		
		notNull("asset identifier",assetId);
		notNull("pull mockDirectives",directives);
		
		double time = System.currentTimeMillis();
		
		Outcome<Codelist> outcome = null;
		
		try {

			Asset asset = repository.lookup(assetId);
			
			//safe: pull mockDirectives are always bound to pull tasks, convince compiler
			@SuppressWarnings("all")
			ImportTask<Asset,ImportDirectives> task = (ImportTask) registry.get(directives);
			
			Codelist parsed = task.retrieve(asset, directives);
			
			outcome = new Outcome<Codelist>(parsed);
			
		}
		catch(Exception e) {
			throw new RuntimeException("cannot import asset "+assetId+" (see cause)",e);
		}
		
		try {
			
			Codelist imported = outcome.result();
		
			listRepository.add(imported);
		
			time = (System.currentTimeMillis()-time)/1000;
		
			log.info("imported codelist '{}' with {} codes in {} secs.",imported.name(),imported.codes().size(),time);
		}
		catch(Exception e) {
			
			log.info("failed importing codelist:\n{}",outcome.report());
		}
		
		return outcome;
	}
}
