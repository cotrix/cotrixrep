package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;
import static org.cotrix.domain.trait.Status.*;

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
import org.cotrix.domain.memory.CodelistMS;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;

/**
 * Builds {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistBuilder implements CodelistNewClause, CodelistChangeClause, ThirdClause, FourthClause,FinalClause {

	private final CodelistMS state;
	
	
	public CodelistBuilder() {
		this.state = new CodelistMS(null);
		this.state.version(new DefaultVersion());
	}
	
	public CodelistBuilder(String id) {
		this.state = new CodelistMS(id);
		state.status(MODIFIED);
	}
	
	@Override
	public SecondClause name(QName name) {
		state.name(name);
		return this;
	}
	
	@Override
	public SecondClause name(String name) {
		return name(Codes.q(name));
	}
	
	@Override
	public Codelist delete() {
		state.status(DELETED);
		return build();
	}
	
	@Override
	public ThirdClause with(List<Code> codes) {
		state.codes(reveal(codes,Code.Private.class));
		return this;
	}
	
	@Override
	public ThirdClause with(Code ... codes) {
		return with(asList(codes));
	}
	
	@Override
	public FourthClause links(CodelistLink... links) {
		state.links(reveal(asList(links),CodelistLink.Private.class));
		return this;
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		return attributes(asList(attributes));
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {	
		state.attributes(reveal(attributes,Attribute.Private.class));
		return this;
	}
	
	public CodelistBuilder version(Version version) {
		state.version(version);
		return this;
	}
	
	public CodelistBuilder version(String version) {
		state.version(new DefaultVersion(version));
		return this;
	}
	
	public Codelist build() {
		return state.entity();
	}
	
}
