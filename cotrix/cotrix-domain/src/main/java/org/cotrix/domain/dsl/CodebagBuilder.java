package org.cotrix.domain.dsl;

import static java.util.Arrays.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.Factory;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.common.BaseGroup;
import org.cotrix.domain.common.Delta;
import org.cotrix.domain.dsl.grammar.CodebagGrammar.CodebagStartClause;
import org.cotrix.domain.dsl.grammar.CodebagGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodebagGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.CodebagGrammar.ThirdClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.pos.CodebagPO;
import org.cotrix.domain.versions.SimpleVersion;
import org.cotrix.domain.versions.Version;

/**
 * Builds {@link Codebag}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodebagBuilder implements CodebagStartClause, SecondClause,ThirdClause,FinalClause {

	private final Factory factory;
	private final CodebagPO po;
	
	CodebagBuilder(Factory factory,String id) {
		this.factory=factory;
		po = new CodebagPO(id);
	}
	
	@Override
	public SecondClause with(QName name) {
		po.setName(name);
		return this;
	}
	
	@Override
	public ThirdClause with(Codelist ... lists) {
		po.setLists(new BaseGroup<Codelist>(asList(lists)));
		return this;
	}
	
	@Override
	public FinalClause and(Attribute ... attributes) {
		po.setAttributes(new BaseBag<Attribute>(asList(attributes)));
		return this;
	}
	
	public CodebagBuilder version(Version version) {
		po.setVersion(version);
		return this;
	}
	
	public CodebagBuilder version(String version) {
		po.setVersion(new SimpleVersion(version));
		return this;
	}
	
	@Override
	public BuildClause<Codebag> as(Delta delta) {
		if (delta!=Delta.NEW && po.id()==null)
			throw new IllegalStateException("object is marked as update but has its identifier is null");
		po.setDelta(delta);
		return this;
	}
	
	public Codebag build() {
		return factory.codebag(po);
	}
	
}
