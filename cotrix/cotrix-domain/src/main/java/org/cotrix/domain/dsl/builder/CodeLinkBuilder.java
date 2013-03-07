package org.cotrix.domain.dsl.builder;

import static org.cotrix.domain.trait.Change.*;
import static org.cotrix.domain.utils.Utils.*;

import java.util.Arrays;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.dsl.grammar.CodeLinkGrammar.CodeLinkStartClause;
import org.cotrix.domain.dsl.grammar.CodeLinkGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.po.CodeLinkPO;
import org.cotrix.domain.primitive.container.Bag;
import org.cotrix.domain.primitive.link.CodeLink;
import org.cotrix.domain.primitive.link.CodelistLink;
import org.cotrix.domain.spi.Factory;
import org.cotrix.domain.trait.Change;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodeLinkBuilder implements CodeLinkStartClause,LinkTargetClause<Code,FinalClause>,FinalClause {

	
	private final CodeLinkPO po;
	private final Factory factory;

	
	public CodeLinkBuilder(Factory factory,String id) {
		this.factory=factory;
		this.po = new CodeLinkPO(id);
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		po.setAttributes(new Bag<Attribute>(Arrays.asList(attributes)));
		return this;
	}
	
	
	@Override
	public LinkTargetClause<Code,FinalClause> instanceOf(CodelistLink def) {
		po.setDefinition(def);
		return this;
	}
	
	
	@Override
	public FinalClause target(Code code) {
		
		notNull(code);

		if (code.id()==null)
			throw new IllegalArgumentException("cannot link to an unidentified code");
		
		po.setTargetId(code.id());
		return this;
	}

	@Override
	public BuildClause<CodeLink> as(Change change) {
		if (change!=NEW && po.id()==null)
			throw new IllegalStateException("object is marked as update but has its identifier is null");
		po.setChange(change);
		return this;
	}
	
	@Override
	public CodeLink build() {
		return factory.codeLink(po);
	}
	

}
