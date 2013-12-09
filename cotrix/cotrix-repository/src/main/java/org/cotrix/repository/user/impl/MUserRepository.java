package org.cotrix.repository.user.impl;

import javax.enterprise.context.ApplicationScoped;

import org.cotrix.domain.user.User;
import org.cotrix.repository.codelist.CodelistRepository;
import org.cotrix.repository.impl.memory.MemoryRepository;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 *
 */
@ApplicationScoped
public class MUserRepository extends MemoryRepository<User.State> {}
