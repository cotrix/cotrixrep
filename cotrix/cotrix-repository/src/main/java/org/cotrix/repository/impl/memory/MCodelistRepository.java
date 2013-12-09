package org.cotrix.repository.impl.memory;

import javax.enterprise.context.ApplicationScoped;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 * 
 */
@ApplicationScoped
public class MCodelistRepository extends MemoryRepository<Codelist.State> {

}
