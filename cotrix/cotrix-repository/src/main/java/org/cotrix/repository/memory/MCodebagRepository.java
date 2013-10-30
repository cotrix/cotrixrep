package org.cotrix.repository.memory;

import javax.inject.Inject;

import org.cotrix.common.Utils;
import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.repository.CodebagRepository;
import org.cotrix.repository.CodelistRepository;

/**
 * An in-memory {@link CodebagRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
public class MCodebagRepository extends MRepository<Codebag,Codebag.Private> implements CodebagRepository {

	private final CodelistRepository listRepository;
	
	/**
	 * Creates an instance over a private {@link MStore}.
	 */
	@Inject
	public MCodebagRepository(IdGenerator generator) {
		this(new MStore(),generator);
	}
	
	/**
	 * Creates an instance over a given {@link MStore}.
	 * @param store
	 */
	public MCodebagRepository(MStore store,IdGenerator generator) {
		super(store,Codebag.class,Codebag.Private.class,generator);
		this.listRepository = new MCodelistRepository(store,generator);
	}
	
	@Override
	public void add(Codebag bag) {

		for (Attribute a: bag.attributes())
				Utils.reveal(a,Attribute.Private.class).setId(generateId());
			
		//propagate addition
		for (Codelist list : bag.lists())
			listRepository.add(list);
		
		super.add(bag);
	}
}
