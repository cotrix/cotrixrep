package org.cotrix.domain.dsl;

import static java.util.Arrays.*;
import static org.cotrix.domain.traits.Change.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistStartClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.ThirdClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.pos.CodelistPO;
import org.cotrix.domain.primitives.BaseBag;
import org.cotrix.domain.primitives.BaseGroup;
import org.cotrix.domain.spi.Factory;
import org.cotrix.domain.traits.Change;
import org.cotrix.domain.versions.SimpleVersion;
import org.cotrix.domain.versions.Version;

/**
 * Builds {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistBuilder implements CodelistStartClause,SecondClause, ThirdClause,FinalClause {

	private final Factory factory;
	private final CodelistPO po;
	
	
	CodelistBuilder(Factory factory,String id) {
		this.factory=factory;
		this.po = new CodelistPO(id);
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
	public CodelistBuilder with(Code ... codes) {
		
		po.setCodes(new BaseGroup<Code>(asList(codes)));
		return this;
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		po.setAttributes(new BaseBag<Attribute>(asList(attributes)));
		return this;
	}
	
	public CodelistBuilder version(Version version) {
		po.setVersion(version);
		return this;
	}
	
	public CodelistBuilder version(String version) {
		po.setVersion(new SimpleVersion(version));
		return this;
	}
	

	@Override
	public BuildClause<Codelist> as(Change change) {
		if (change!=NEW && po.id()==null)
			throw new IllegalStateException("object is marked as update but has its identifier is null");
		po.setChange(change);
		return this;
	}
	
	public Codelist build() {
		return factory.codelist(po);
	}
	
}
