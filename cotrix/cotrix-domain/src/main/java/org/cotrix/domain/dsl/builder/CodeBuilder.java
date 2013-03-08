package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.domain.trait.Change.*;

import java.util.Arrays;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeStartClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.po.CodePO;
import org.cotrix.domain.spi.Factory;
import org.cotrix.domain.trait.Change;

/**
 * Builds {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodeBuilder implements CodeStartClause,SecondClause,FinalClause {

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
	public SecondClause name(QName name) {
		po.setName(name);
		return this;
	}

	
	@Override
	public SecondClause name(String name) {
		return name(Codes.q(name));
	}

	@Override
	public FinalClause links(Codelink... links) {
		po.setLinks(asList(links));
		return this;
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		po.setAttributes(Arrays.asList(attributes));
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
