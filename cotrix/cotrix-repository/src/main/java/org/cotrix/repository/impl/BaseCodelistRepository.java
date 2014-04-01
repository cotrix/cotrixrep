package org.cotrix.repository.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
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
		
		checkLinks(list);
		
		super.add(list);
	}
	
	@Override
	public void update(Codelist changeset) {
		
		checkLinks(changeset);
		
		super.update(changeset);
	}
	
	
	private void checkLinks(Codelist list) {
		
		for (CodelistLink link : list.links()) {
			Codelist target = link.target();
			//could be checking a changeset with no target 
 			if (target!=null && lookup(link.target().id())==null)
				throw new IllegalArgumentException("list "+list.id()+"has an invalid link: target "+list.id()+" is unknown ");
		}
	}
}
