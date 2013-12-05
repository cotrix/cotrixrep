package org.cotrix.repository.codelist.impl;

import javax.enterprise.context.ApplicationScoped;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.codelist.CodelistRepository;
import org.cotrix.repository.impl.memory.MemoryRepository;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 * 
 */
@ApplicationScoped
public class MCodelistRepository extends MemoryRepository<Codelist.Private> {


}
