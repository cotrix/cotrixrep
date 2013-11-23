package org.cotrix.repository.memory;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.QueryFactory;
import org.cotrix.repository.query.CodebagQuery;
import org.cotrix.repository.query.CodelistQuery;

/**
 * A {@link QueryFactory} for {@link MQuery}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class MQueryFactory implements QueryFactory {

	static abstract class CodelistMQuery<R> extends MQuery<Codelist, R> implements CodelistQuery<R> {}
	static abstract class CodebagMQuery<R> extends MQuery<Codebag, R> implements CodebagQuery<R> {}

	
	@Override
	public CodelistQuery<Codelist> allLists() {
		
		return new CodelistMQuery<Codelist>() {
			public Collection<Codelist> executeOn(MRepository<Codelist,?> repository) {
				return repository.getAll();
			}
		};
	}
	
	@Override
	public CodebagQuery<Codebag> allBags() {
		return new CodebagMQuery<Codebag>() {
			public Collection<Codebag> executeOn(MRepository<Codebag,?> repository) {
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
}
