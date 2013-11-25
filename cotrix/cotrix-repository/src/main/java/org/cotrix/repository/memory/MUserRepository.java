package org.cotrix.repository.memory;

import org.cotrix.domain.User;
import org.cotrix.repository.CodelistRepository;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
public class MUserRepository extends MemoryRepository<User.Private> {}
