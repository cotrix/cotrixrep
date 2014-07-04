package org.cotrix.repository.impl;

import static java.lang.System.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.spi.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class BaseCodelistRepository extends AbstractRepository<Codelist,Codelist.Private,Codelist.State> implements CodelistRepository {

	//logs 'on behalf' of interface, for clarity
	private static Logger log = LoggerFactory.getLogger(CodelistRepository.class);
	
	@Inject
	public BaseCodelistRepository(StateRepository<Codelist.State> repository, EventProducer producer) {
		super(repository,producer);
	}
	
	@Override
	public void add(Codelist list) {
		
		long time = System.currentTimeMillis();
		
		super.add(list);
		
		time = currentTimeMillis()-time;
		
		log.trace("added codelist {} in {} msecs, roughly {} codes/sec",list.id(),time,((float) list.codes().size()/time*1000));
		
		
		
	}
}
