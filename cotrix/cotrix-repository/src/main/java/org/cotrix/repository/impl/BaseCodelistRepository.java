package org.cotrix.repository.impl;

import static java.lang.System.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.SecondClause;
import org.cotrix.domain.utils.DomainUtils;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.spi.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class BaseCodelistRepository extends AbstractRepository<Codelist,Codelist.Private,Codelist.Bean> implements CodelistRepository {

	//logs 'on behalf' of interface, for clarity
	private static Logger log = LoggerFactory.getLogger(CodelistRepository.class);
	
	@Inject
	public BaseCodelistRepository(StateRepository<Codelist.Bean> repository, EventProducer producer) {
		super(repository,producer);
	}
	
	@Override
	public void add(Codelist list) {
		
		long time = System.currentTimeMillis();
		
		super.add(list);
		
		time = currentTimeMillis()-time;
		
		log.trace("added codelist {} in {} msecs, roughly {} codes/sec",DomainUtils.signatureOf(list),time,((float) list.codes().size()/time*1000));
		
	}
	
	@Override
	public void add(SecondClause list) {
		add(list.build());
	}
	@Override
	public void update(SecondClause changeset) {
		update(changeset.build());
	}
}
