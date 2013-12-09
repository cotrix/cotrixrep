package org.cotrix.repository.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;

@ApplicationScoped
public class DefaultCodelistRepository extends AbstractRepository<Codelist,Codelist.Private,Codelist.State> implements CodelistRepository {

	@Inject
	public DefaultCodelistRepository(StateRepository<Codelist.State> repository) {
		super(repository);
	}
}
