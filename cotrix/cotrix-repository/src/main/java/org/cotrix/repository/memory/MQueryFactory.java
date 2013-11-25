package org.cotrix.repository.memory;

import static org.cotrix.domain.utils.Constants.*;
import static org.cotrix.repository.CodelistCoordinates.*;
import static org.cotrix.repository.CodelistSummary.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistCoordinates;
import org.cotrix.repository.CodelistQuery;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.repository.QueryFactory;
import org.cotrix.repository.Specification;

/**
 * A {@link QueryFactory} for {@link MQuery}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class MQueryFactory implements QueryFactory {

	static abstract class CodelistMQuery<R> extends MQuery<Codelist, R> implements CodelistQuery<R> {}
	
	
	@Override
	public CodelistQuery<Codelist> allLists() {
		
		return new CodelistMQuery<Codelist>() {
			public Collection<? extends Codelist> executeOn(MemoryRepository<? extends Codelist> repository) {
				return repository.getAll();
			}
		};
	}
	
	@Override
	public CodelistQuery<Code> allCodes(final String codelistId) {
		return new CodelistMQuery<Code>() {
			public Collection<? extends Code> executeOn(MemoryRepository<? extends Codelist> repository) {
				Collection<Code> codes = new ArrayList<Code>();
				for (Code c : repository.lookup(codelistId).codes())
					codes.add(c);
				return codes;
			}
		};
	}
	
	@Override
	public CodelistQuery<CodelistCoordinates> allListCoordinates() {
		return new CodelistMQuery<CodelistCoordinates>() {
			public Collection<CodelistCoordinates> executeOn(MemoryRepository<? extends Codelist> repository) {
				Collection<CodelistCoordinates> coordinates = new HashSet<CodelistCoordinates>();
				for (Codelist list : repository.getAll())
					coordinates.add(coords(list.id(),list.name(),list.version()));
				return coordinates;
			}
		};
	}
	
	
	public Specification<Codelist,CodelistSummary> summary(final String id) {
		
		return new MSpecification<Codelist, CodelistSummary>() {
			@Override
			public CodelistSummary execute(MemoryRepository<? extends Codelist> repository) {
				
				Codelist list = repository.lookup(id);

				if (list == null)
					throw new IllegalStateException("no such codelist: " + id);

				int size = list.codes().size();

				Collection<Attribute> attributes = new ArrayList<Attribute>();
				for (Attribute a : list.attributes())
					if (!a.type().equals(SYSTEM_TYPE))
						attributes.add(a);
				
				Map<QName,Map<QName,Set<String>>> fingerprint = new HashMap<QName, Map<QName,Set<String>>>();
				
				for (Code c : list.codes())
					addToFingerprint(fingerprint,c.attributes());
				
				return new CodelistSummary(list.name(), size, attributes, fingerprint);
			}
		};
	}
}
