package org.cotrix.repository.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.Repository;

@ApplicationScoped
public class DefaultCodelistRepository extends AbstractRepository<Codelist,Codelist.Private,Codelist.State> implements CodelistRepository {

	@Inject
	public DefaultCodelistRepository(Repository<Codelist.State> repository) {
		super(repository);
	}
}
