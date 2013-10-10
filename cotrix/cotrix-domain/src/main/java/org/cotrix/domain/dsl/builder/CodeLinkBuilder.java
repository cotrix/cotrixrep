package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.trait.Change.*;

import java.util.Arrays;
import java.util.List;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.dsl.grammar.CodeLinkGrammar.CodeLinkStartClause;
import org.cotrix.domain.dsl.grammar.CodeLinkGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.CodeLinkClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.DeltaClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.po.CodeLinkPO;
import org.cotrix.domain.trait.Change;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodeLinkBuilder implements CodeLinkStartClause,LinkTargetClause<Code,FinalClause>,DeltaClause<CodeLinkClause<FinalClause>, CodeLinkClause<FinalClause>,Codelink> ,FinalClause {

	
	private final CodeLinkPO po;
	
	
	public CodeLinkBuilder(String id) {
		this.po = new CodeLinkPO(id);
	}
	
	@Override
	public Codelink delete() {
		return this.as(DELETED).build();
	}
	
	
	@Override
	public CodeLinkClause<FinalClause> add() {
		return this.as(NEW);
	}
	
	@Override
	public CodeLinkClause<FinalClause> modify() {
		return this.as(MODIFIED);
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		po.setAttributes(Arrays.asList(attributes));
		return this;
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {
		po.setAttributes(attributes);
		return this;
	}
	
	@Override
	public LinkTargetClause<Code,FinalClause> instanceOf(CodelistLink def) {
		po.setDefinition(def);
		return this;
	}
	
	
	@Override
	public FinalClause target(Code code) {
		
		notNull("code",code);

		if (code.id()==null)
			throw new IllegalArgumentException("cannot link to an unidentified code");
		
		po.setTargetId(code.id());
		return this;
	}

	private CodeLinkBuilder as(Change change) {
		if (change!=NEW && po.id()==null)
			throw new IllegalStateException("object is marked as update but has its identifier is null");
		po.setChange(change);
		return this;
	}
	
	@Override
	public Codelink build() {
		return new Codelink.Private(po);
	}
	

}
