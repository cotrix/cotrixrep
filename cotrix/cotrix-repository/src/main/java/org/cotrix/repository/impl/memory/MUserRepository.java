package org.cotrix.repository.impl.memory;

import javax.enterprise.context.ApplicationScoped;

import org.cotrix.domain.user.User;
import org.cotrix.repository.CodelistRepository;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
@ApplicationScoped
public class MUserRepository extends MemoryRepository<User.State> {}
