package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.AttributeDefinitionGrammar;
import org.cotrix.domain.dsl.grammar.AttributeDefinitionGrammar.OptionalClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistNewClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.SecondClause;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.memory.MCodelist;
import org.cotrix.domain.version.DefaultVersion;
import org.cotrix.domain.version.Version;

/**
 * Builds {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodelistBuilder implements CodelistNewClause, CodelistChangeClause {

	private final MCodelist state;
	
	
	public CodelistBuilder(MCodelist state) {
		this.state = state;
	}
	
	@Override
	public SecondClause name(QName name) {
		state.qname(name);
		return this;
	}
	
	@Override
	public SecondClause name(String name) {
		return name(Codes.q(name));
	}
	
	@Override
	public CodelistBuilder with(Iterable<Code> codes) {
		state.codes(reveal(codes,Code.Private.class));
		return this;
	}
	
	@Override
	public SecondClause with(CodeGrammar.OptionalClause ... clauses) {
		
		Collection<Code> codes = new ArrayList<Code>();
		
		for (CodeGrammar.OptionalClause clause : clauses)
			codes.add(clause.build());
				
		return with(codes);
	}
	
	@Override
	public CodelistBuilder definitions(AttributeDefinition ... defs) {
		return definitions(asList(defs));
	}
	
	@Override
	public SecondClause definitions(OptionalClause... clauses) {
		
		Collection<AttributeDefinition> defs = new ArrayList<AttributeDefinition>();
		
		for (AttributeDefinitionGrammar.OptionalClause clause : clauses)
			defs.add(clause.build());
				
		return definitions(defs);
	}
	
	@Override
	public CodelistBuilder definitions(Iterable<AttributeDefinition> types) {
		state.definitions(reveal(types,AttributeDefinition.Private.class));
		return this;
	}
	
	@Override
	public CodelistBuilder with(Code ... codes) {
		return with(asList(codes));
	}
	
	@Override
	public CodelistBuilder links(LinkDefinition... links) {
		return links(asList(links));
	}
	
	@Override
	public CodelistBuilder links(Collection<LinkDefinition> links) {
		state.links(reveal(links,LinkDefinition.Private.class));
		return this;
	}
	
	@Override
	public CodelistBuilder attributes(Attribute ... attributes) {
		return attributes(asList(attributes));
	}
	
	@Override
	public CodelistBuilder attributes(Collection<Attribute> attributes) {	
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
