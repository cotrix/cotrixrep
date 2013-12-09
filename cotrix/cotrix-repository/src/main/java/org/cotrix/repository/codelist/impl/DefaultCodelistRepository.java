package org.cotrix.repository.codelist.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.Repository;
import org.cotrix.repository.codelist.CodelistRepository;
import org.cotrix.repository.impl.AbstractRepository;

@ApplicationScoped
public class DefaultCodelistRepository extends AbstractRepository<Codelist,Codelist.Private,Codelist.State> implements CodelistRepository {

	@Inject
	public DefaultCodelistRepository(Repository<Codelist.State> repository) {
		super(repository);
	}
}
