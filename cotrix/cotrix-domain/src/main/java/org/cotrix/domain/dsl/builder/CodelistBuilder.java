package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.trait.Status.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistNewClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.FourthClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.ThirdClause;
import org.cotrix.domain.po.CodelistPO;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;

/**
 * Builds {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistBuilder implements CodelistNewClause, CodelistChangeClause, ThirdClause, FourthClause,FinalClause {

	private final CodelistPO po;
	
	
	public CodelistBuilder() {
		this.po = new CodelistPO(null);
		this.po.version(new DefaultVersion());
	}
	
	public CodelistBuilder(String id) {
		this.po = new CodelistPO(id);
		po.status(MODIFIED);
	}
	
	@Override
	public SecondClause name(QName name) {
		po.name(name);
		return this;
	}
	
	@Override
	public SecondClause name(String name) {
		return name(Codes.q(name));
	}
	
	@Override
	public Codelist delete() {
		po.status(DELETED);
		return build();
	}
	
	@Override
	public ThirdClause with(List<Code> codes) {
		
		List<Code.State> state = new ArrayList<Code.State>();
		
		for (Code a : codes)
			state.add(reveal(a,Code.Private.class).state());
		
		po.codes(state);
		
		return this;
	}
	
	@Override
	public ThirdClause with(Code ... codes) {
		return with(asList(codes));
	}
	
	@Override
	public FourthClause links(CodelistLink... links) {
		List<CodelistLink.State> state = new ArrayList<CodelistLink.State>();
		
		for (CodelistLink a : links)
			state.add(reveal(a,CodelistLink.Private.class).state());
		
		po.links(state);
		
		return this;
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		return attributes(asList(attributes));
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {
		List<Attribute.State> state = new ArrayList<Attribute.State>();
		
		for (Attribute a : attributes)
			state.add(reveal(a,Attribute.Private.class).state());
		
		po.attributes(state);
		return this;
	}
	
	public CodelistBuilder version(Version version) {
		po.version(version);
		return this;
	}
	
	public CodelistBuilder version(String version) {
		po.version(new DefaultVersion(version));
		return this;
	}
	
	public Codelist build() {
		return new Codelist.Private(po);
	}
	
}
