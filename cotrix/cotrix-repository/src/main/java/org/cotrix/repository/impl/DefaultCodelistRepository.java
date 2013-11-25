package org.cotrix.repository.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.Repository;
import org.cotrix.repository.codelist.CodelistRepository;

@ApplicationScoped
public class DefaultCodelistRepository extends DefaultRepository<Codelist,Codelist.Private,Repository<Codelist.Private>> implements CodelistRepository {

	@Inject
	public DefaultCodelistRepository(Repository<Codelist.Private> repository) {
		super(repository,Codelist.class,Codelist.Private.class);
	}
}
