package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;

import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistNewClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.SecondClause;
import org.cotrix.domain.memory.CodelistMS;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;

/**
 * Builds {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistBuilder implements CodelistNewClause, CodelistChangeClause {

	private final CodelistMS state;
	
	
	public CodelistBuilder(CodelistMS state) {
		this.state = state;
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
	public CodelistBuilder with(List<Code> codes) {
		state.codes(reveal(codes,Code.Private.class));
		return this;
	}
	
	@Override
	public CodelistBuilder with(Code ... codes) {
		return with(asList(codes));
	}
	
	@Override
	public CodelistBuilder links(CodelistLink... links) {
		return links(asList(links));
	}
	
	@Override
	public CodelistBuilder links(Collection<CodelistLink> links) {
		state.links(reveal(links,CodelistLink.Private.class));
		return this;
	}
	
	@Override
	public CodelistBuilder attributes(Attribute ... attributes) {
		return attributes(asList(attributes));
	}
	
	@Override
	public CodelistBuilder attributes(List<Attribute> attributes) {	
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
