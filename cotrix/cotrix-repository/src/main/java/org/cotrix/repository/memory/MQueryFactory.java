package org.cotrix.repository.memory;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.QueryFactory;
import org.cotrix.repository.query.Query;

/**
 * A {@link QueryFactory} for {@link MQuery}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class MQueryFactory implements QueryFactory {

	
	@Override
	public Query<Codelist,Codelist> allLists() {
		return new MQuery<Codelist,Codelist>() {
			public Iterable<Codelist> _execute(MRepository<Codelist,?> repository) {
				return repository.getAll();
			}
		};
	}
	
	@Override
	public Query<Codebag,Codebag> allBags() {
		return new MQuery<Codebag,Codebag>() {
			public Iterable<Codebag> _execute(MRepository<Codebag,?> repository) {
				return repository.getAll();
			}
		};
	}
	
	@Override
	public Query<Codelist, Code> allCodes(final String codelistId) {
		return new MQuery<Codelist,Code>() {
			public Iterable<? extends Code> _execute(MRepository<Codelist,?> repository) {
				return repository.lookup(codelistId).codes();
			}
		};
	}
}
