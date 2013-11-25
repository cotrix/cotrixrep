package org.cotrix.repository.memory;

import org.cotrix.domain.user.User;
import org.cotrix.repository.codelist.CodelistRepository;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
public class MUserRepository extends MemoryRepository<User.Private> {}
