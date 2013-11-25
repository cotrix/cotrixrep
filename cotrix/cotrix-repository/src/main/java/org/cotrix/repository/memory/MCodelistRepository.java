package org.cotrix.repository.memory;

import javax.inject.Inject;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.repository.CodelistRepository;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 * 
 */
public class MCodelistRepository extends MemoryRepository<Codelist.Private> {

	/**
	 * Creates an instance over a private {@link MStore}.
	 */
	@Inject
	public MCodelistRepository(IdGenerator generator) {
		super(generator);
	}

	@Override
	public void add(Codelist.Private list) {

		super.add(list);

		for (Attribute.Private a : list.attributes())
			a.setId(generateId());

		for (CodelistLink.Private l : list.links())
			l.setId(generateId());

		for (Code.Private c : list.codes()) {
			for (Attribute.Private a : c.attributes())
				a.setId(generateId());

			for (Codelink.Private l : c.links())
				l.setId(generateId());

			c.setId(generateId());
		}

	}
}
