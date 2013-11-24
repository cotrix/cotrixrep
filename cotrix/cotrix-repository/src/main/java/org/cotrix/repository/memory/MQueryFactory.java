package org.cotrix.repository.memory;

import static org.cotrix.repository.query.CodelistCoordinates.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.QueryFactory;
import org.cotrix.repository.query.CodelistCoordinates;
import org.cotrix.repository.query.CodelistQuery;

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
			public Collection<Codelist> executeOn(MRepository<Codelist,?> repository) {
				return repository.getAll();
			}
		};
	}
	
	@Override
	public CodelistQuery<Code> allCodes(final String codelistId) {
		return new CodelistMQuery<Code>() {
			public Collection<? extends Code> executeOn(MRepository<Codelist,?> repository) {
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
			public Collection<CodelistCoordinates> executeOn(MRepository<Codelist,?> repository) {
				Collection<CodelistCoordinates> coordinates = new HashSet<CodelistCoordinates>();
				for (Codelist list : repository.getAll())
					coordinates.add(coords(list.id(),list.name(),list.version()));
				return coordinates;
			}
		};
	}
}
