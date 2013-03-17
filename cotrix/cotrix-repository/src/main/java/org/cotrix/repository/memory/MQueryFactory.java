package org.cotrix.repository.memory;

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
			public Codelist yieldFor(Codelist list) {
				return list;
			}
		};
	}
	
	@Override
	public Query<Codebag,Codebag> allBags() {
		return new MQuery<Codebag,Codebag>() {
			public Codebag yieldFor(Codebag bag) {
				return bag;
			}
		};
	}
}
