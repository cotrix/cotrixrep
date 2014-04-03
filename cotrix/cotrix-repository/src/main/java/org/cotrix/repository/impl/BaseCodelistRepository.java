package org.cotrix.repository.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.spi.StateRepository;

@ApplicationScoped
public class BaseCodelistRepository extends AbstractRepository<Codelist,Codelist.Private,Codelist.State> implements CodelistRepository {

	@Inject
	public BaseCodelistRepository(StateRepository<Codelist.State> repository) {
		super(repository);
	}
	
	@Override
	public void add(Codelist list) {
		
		super.add(list);
	}
	
	@Override
	public void update(Codelist changeset) {
		
		super.update(changeset);
	}
}
