package org.cotrix.repository.memory;

import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.repository.codelist.CodelistRepository;

/**
 * An in-memory {@link CodelistRepository}.
 * 
 * @author Fabio Simeoni
 * 
 */
@Singleton
public class MCodelistRepository extends MemoryRepository<Codelist.Private> {

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
