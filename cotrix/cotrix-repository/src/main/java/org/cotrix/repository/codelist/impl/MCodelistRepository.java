package org.cotrix.repository.codelist.impl;

import javax.enterprise.context.ApplicationScoped;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
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

	@Override
	public void add(Codelist.Private list) {

		super.add(list);

		for (Attribute.Private a : list.attributes())
			a.id(generateId());

		for (CodelistLink.Private l : list.links())
			l.id(generateId());

		for (Code.Private c : list.codes()) {
			for (Attribute.Private a : c.attributes())
				a.id(generateId());

			for (Codelink.Private l : c.links())
				l.id(generateId());

			c.id(generateId());
		}

	}
}
