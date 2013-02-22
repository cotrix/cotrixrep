package org.cotrix.domain.dsl;

import java.util.Arrays;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Factory;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.common.Delta;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeStartClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.pos.CodePO;

/**
 * Builds {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodeBuilder implements CodeStartClause,FinalClause {

	private final Factory factory;
	private final CodePO po;
	
	public CodeBuilder(Factory factory) {
		this(factory,factory.generateId());

	}
	
	public CodeBuilder(Factory factory,String id) {
		this.factory=factory;
		po = new CodePO(id);
	}
	
	
	@Override
	public FinalClause with(QName name) {
		po.setName(name);
		return this;
	}

	@Override
	public FinalClause and(Attribute ... attributes) {
		po.setAttributes(new BaseBag<Attribute>(Arrays.asList(attributes)));
		return this;
	}

	@Override
	public BuildClause<Code> as(Delta delta) {
		if (delta!=Delta.NEW && po.id()==null)
			throw new IllegalStateException("object is marked as update but has its identifier is null");
		po.setDelta(delta);
		return this;
	}
	
	public Code build() {
		return factory.code(po);
	}
	
}
