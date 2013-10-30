package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.domain.trait.Status.*;

import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.ChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistStartClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.FourthClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.ThirdClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.DeltaClause;
import org.cotrix.domain.po.CodelistPO;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;

/**
 * Builds {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistBuilder implements CodelistStartClause, ChangeClause, DeltaClause<ChangeClause,Codelist>, SecondClause,ThirdClause,FourthClause,FinalClause {

	private final CodelistPO po;
	
	
	public CodelistBuilder(String id) {
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
	public Codelist delete() {
		po.setChange(DELETED);
		return build();
	}
	
	@Override
	public ChangeClause modify() {
		po.setChange(MODIFIED);
		return this;
	}
	
	@Override
	public ThirdClause with(List<Code> codes) {
		po.setCodes(codes);
		return this;
	}
	
	@Override
	public ThirdClause with(Code ... codes) {
		po.setCodes(asList(codes));
		return this;
	}
	
	@Override
	public FourthClause links(CodelistLink... links) {
		po.setLinks(asList(links));
		return this;
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		po.setAttributes(asList(attributes));
		return this;
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {
		po.setAttributes(attributes);
		return this;
	}
	
	public CodelistBuilder version(Version version) {
		po.setVersion(version);
		return this;
	}
	
	public CodelistBuilder version(String version) {
		po.setVersion(new DefaultVersion(version));
		return this;
	}
	
	public Codelist build() {
		return new Codelist.Private(po);
	}
	
}
