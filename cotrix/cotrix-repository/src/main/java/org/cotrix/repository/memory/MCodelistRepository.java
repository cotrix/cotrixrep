package org.cotrix.repository.memory;

import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistRepository;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
public class MCodelistRepository extends MRepository<Codelist, Codelist.Private> implements CodelistRepository {

	/**
	 * Creates an instance over a private {@link MStore}.
	 */
	public MCodelistRepository() {
		this(new MStore());
	}
	
	/**
	 * Creates an instance over a given {@link MStore}.
	 * @param store
	 */
	public MCodelistRepository(MStore store) {
		super(store,Codelist.class,Codelist.Private.class);
	}
}
