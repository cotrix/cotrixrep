package org.cotrix.user.memory;

import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.memory.MemoryRepository;
import org.cotrix.user.User;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
public class MUserRepository extends MemoryRepository<User.Private> {}
