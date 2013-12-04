package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.trait.Status.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.dsl.grammar.CodeLinkGrammar.CodeLinkStartClause;
import org.cotrix.domain.dsl.grammar.CodeLinkGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.DeleteClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.memory.CodelinkMS;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodeLinkBuilder implements CodeLinkStartClause,LinkTargetClause<Code,FinalClause>,DeleteClause<Codelink> ,FinalClause {

	
	private final CodelinkMS po;
	
	
	public CodeLinkBuilder() {
		this.po = new CodelinkMS(null);
	}
	
	public CodeLinkBuilder(String id) {
		this.po = new CodelinkMS(id);
		po.status(MODIFIED);
	}
	
	@Override
	public Codelink delete() {
		po.status(DELETED);
		return build();
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		return attributes(Arrays.asList(attributes));
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {

		List<Attribute.State> state = new ArrayList<Attribute.State>();
		
		for (Attribute a : attributes)
			state.add(reveal(a,Attribute.Private.class).state());
		
		po.attributes(state);
		
		return this;
	}
	
	@Override
	public LinkTargetClause<Code,FinalClause> instanceOf(CodelistLink def) {
		po.definition(def);
		return this;
	}
	
	
	@Override
	public FinalClause target(Code code) {
		
		notNull("code",code);

		if (code.id()==null)
			throw new IllegalArgumentException("cannot link to an unidentified code");
		
		po.targetId(code.id());
		return this;
	}
	
	@Override
	public Codelink build() {
		return po.entity();
	}
	

}
