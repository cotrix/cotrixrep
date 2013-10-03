package org.cotrix.io.publish;

import static org.cotrix.common.Utils.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.io.PublicationService;
import org.cotrix.io.utils.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link PublicationService} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
@Singleton
public class DefaultPublicationService implements PublicationService {

	private static final Logger log  = LoggerFactory.getLogger(PublicationService.class);
	
	private Registry<PublicationTask<PublicationDirectives>> registry;
	
	@Inject
	public DefaultPublicationService(Registry<PublicationTask<PublicationDirectives>> registry) {
		
		this.registry=registry;
		
		log.info("configured with tasks {}",registry.getAll());
	}
	
	@Override
	public void publish(Codelist codelist, PublicationDirectives directives, QName channel) {
		
		valid(codelist);
		
		notNull("mockDirectives",directives);
		notNull("channel's name",channel);
		
		try {

			PublicationTask<PublicationDirectives> task = registry.get(directives);
			
			task.publish(channel,codelist,directives);
			
		}
		catch(Exception e) {
			throw new IllegalArgumentException("codelist "+codelist.name()+" ("+codelist.id()+") can't be published through channel "+channel+" (see cause)",e);
		}
		
		
		
	}
	
	//helpers
	
	private void valid(Codelist codelist) throws IllegalArgumentException {
		
		notNull("codelist",codelist);
		
		if (codelist.id()==null)
			throw new IllegalArgumentException("codelist has no identifier, have you persisted it yet?");
		
		if (codelist.version()==null)
			throw new IllegalArgumentException("codelist "+codelist.id()+" has no version and cannot be published?");

	}
	

}
