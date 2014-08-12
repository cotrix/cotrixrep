package org.cotrix.repository;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.grammar.CodelistGrammar;


/**
 * 
 * A {@link Repository} of {@link Codelist}s
 * 
 * @author Fabio Simeoni
 *
 */
public interface CodelistRepository extends Repository<Codelist> {
	
	@Override
	public void remove(String id) throws UnremovableCodelistException;
	

	@SuppressWarnings("serial")
	static class UnremovableCodelistException extends IllegalStateException {
		
		public UnremovableCodelistException(String msg) {
			super(msg);
		}
		
		public UnremovableCodelistException(String msg, Exception e) {
			super(msg,e);
		}
	}
	
	//conveniences
	
	public void add(CodelistGrammar.SecondClause list);
	public void update(CodelistGrammar.SecondClause changeset);

}
