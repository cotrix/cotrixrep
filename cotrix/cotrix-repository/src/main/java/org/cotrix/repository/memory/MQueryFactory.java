package org.cotrix.repository.memory;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.QueryFactory;
import org.cotrix.repository.memory.MQuery.CodebagMQuery;
import org.cotrix.repository.memory.MQuery.CodelistMQuery;
import org.cotrix.repository.query.CodebagQuery;
import org.cotrix.repository.query.CodelistQuery;

/**
 * A {@link QueryFactory} for {@link MQuery}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class MQueryFactory implements QueryFactory {

	
	@Override
	public CodelistQuery<Codelist> allLists() {
		
		return new CodelistMQuery<Codelist>() {
			public Iterable<Codelist> _execute(MRepository<Codelist,?> repository) {
				return repository.getAll();
			}
		};
	}
	
	@Override
	public CodebagQuery<Codebag> allBags() {
		return new CodebagMQuery<Codebag>() {
			public Iterable<Codebag> _execute(MRepository<Codebag,?> repository) {
				return repository.getAll();
			}
		};
	}
	
	@Override
	public CodelistQuery<Code> allCodes(final String codelistId) {
		return new CodelistMQuery<Code>() {
			public Iterable<? extends Code> _execute(MRepository<Codelist,?> repository) {
				return repository.lookup(codelistId).codes();
			}
		};
	}
}
