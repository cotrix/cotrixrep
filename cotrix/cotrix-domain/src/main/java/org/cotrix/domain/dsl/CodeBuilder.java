package org.cotrix.domain.dsl;

import static org.cotrix.domain.traits.Change.*;

import java.util.Arrays;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeStartClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.pos.CodePO;
import org.cotrix.domain.primitives.BaseBag;
import org.cotrix.domain.spi.Factory;
import org.cotrix.domain.traits.Change;

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
	public FinalClause name(QName name) {
		po.setName(name);
		return this;
	}

	
	@Override
	public FinalClause name(String name) {
		return name(Codes.q(name));
	}

	@Override
	public FinalClause and(Attribute ... attributes) {
		po.setAttributes(new BaseBag<Attribute>(Arrays.asList(attributes)));
		return this;
	}

	@Override
	public BuildClause<Code> as(Change change) {
		if (change!=NEW && po.id()==null)
			throw new IllegalStateException("object is marked as update but has its identifier is null");
		po.setChange(change);
		return this;
	}
	
	public Code build() {
		return factory.code(po);
	}
	
}
