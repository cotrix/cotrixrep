package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;

import java.util.List;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.CodelinkStartClause;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.memory.CodelinkMS;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelinkBuilder implements CodelinkStartClause,LinkTargetClause<Code,FinalClause>, FinalClause {

	
	private final CodelinkMS state;
	
	
	public CodelinkBuilder(CodelinkMS state) {
		this.state = state;
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		return attributes(asList(attributes));
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {
		state.attributes(reveal(attributes,Attribute.Private.class));
		return this;
	}
	
	@Override
	public LinkTargetClause<Code,FinalClause> instanceOf(CodelistLink def) {
		state.definition(def);
		return this;
	}
	
	
	@Override
	public FinalClause target(Code code) {
		
		notNull("code",code);

		if (code.id()==null)
			throw new IllegalArgumentException("cannot link to an unidentified code");
		
		state.target(code.id());
		return this;
	}
	
	@Override
	public Codelink build() {
		return state.entity();
	}
	

}
