package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.domain.trait.Change.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistStartClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.FourthClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.ThirdClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.po.CodelistPO;
import org.cotrix.domain.primitive.container.Bag;
import org.cotrix.domain.primitive.link.CodelistLink;
import org.cotrix.domain.spi.Factory;
import org.cotrix.domain.trait.Change;
import org.cotrix.domain.version.SimpleVersion;
import org.cotrix.domain.version.Version;

/**
 * Builds {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistBuilder implements CodelistStartClause,SecondClause,ThirdClause,FourthClause,FinalClause {

	private final Factory factory;
	private final CodelistPO po;
	
	
	public CodelistBuilder(Factory factory,String id) {
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
		
		po.setCodes(new Bag<Code>(asList(codes)));
		return this;
	}
	
	@Override
	public FourthClause links(CodelistLink... links) {
		po.setLinks(new Bag<CodelistLink>(asList(links)));
		return this;
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		po.setAttributes(new Bag<Attribute>(asList(attributes)));
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
