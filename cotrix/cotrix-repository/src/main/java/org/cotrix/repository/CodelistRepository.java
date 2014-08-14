package org.cotrix.repository;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.grammar.CodelistGrammar;


public interface CodelistRepository extends Repository<Codelist> {
	
	@Override
	public void remove(String id) throws UnremovableCodelistException; //is linked
	

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
