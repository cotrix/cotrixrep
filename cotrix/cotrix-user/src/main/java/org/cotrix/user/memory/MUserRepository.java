package org.cotrix.user.memory;

import javax.inject.Inject;

import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.memory.MemoryRepository;
import org.cotrix.repository.utils.UuidGenerator;
import org.cotrix.user.User;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
public class MUserRepository extends MemoryRepository<User.Private> {

	/**
	 * Creates an instance over a private {@link MStore}.
	 */
	@Inject
	public MUserRepository(IdGenerator generator) {
		super(generator);
	}	
	
	
	public MUserRepository() {
		super(new UuidGenerator());
	}
	
}
