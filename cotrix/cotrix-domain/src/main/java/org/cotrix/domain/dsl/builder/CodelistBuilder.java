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
import org.cotrix.domain.codelist.DefaultVersion;
import org.cotrix.domain.codelist.Version;
import org.cotrix.domain.dsl.Data;
import org.cotrix.domain.dsl.grammar.AttributeDefinitionGrammar;
import org.cotrix.domain.dsl.grammar.AttributeGrammar;
import org.cotrix.domain.dsl.grammar.CodeGrammar;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.CodelistNewClause;
import org.cotrix.domain.dsl.grammar.CodelistGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.memory.MCodelist;


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
		return name(Data.q(name));
	}
	
	@Override
	public CodelistBuilder with(Iterable<Code> codes) {
		state.codes(reveal(codes,Code.Private.class));
		return this;
	}
	
	@Override
	public SecondClause with(CodeGrammar.SecondClause ... clauses) {
		
		Collection<Code> codes = new ArrayList<Code>();
		
		for (CodeGrammar.SecondClause clause : clauses)
			codes.add(clause.build());
				
		return with(codes);
	}
	
	@Override
	public CodelistBuilder definitions(AttributeDefinition ... defs) {
		return definitions(asList(defs));
	}
	
	@Override
	public SecondClause definitions(AttributeDefinitionGrammar.SecondClause... clauses) {
		
		Collection<AttributeDefinition> defs = new ArrayList<AttributeDefinition>();
		
		for (AttributeDefinitionGrammar.SecondClause clause : clauses)
			defs.add(clause.build());
				
		return definitions(defs);
	}
	
	@Override
	public CodelistBuilder definitions(Iterable<AttributeDefinition> types) {
		state.attributeDefinitions(reveal(types,AttributeDefinition.Private.class));
		return this;
	}
	
	@Override
	public CodelistBuilder with(Code ... codes) {
		return with(asList(codes));
	}
	
	@Override
	public CodelistBuilder links(LinkDefinition... defs) {
		return links(asList(defs));
	}
	
	@Override
	public CodelistBuilder links(Iterable<LinkDefinition> defs) {
		state.linkDefinitions(reveal(defs,LinkDefinition.Private.class));
		return this;
	}
	
	@Override
	public SecondClause links(LinkDefinitionGrammar.ThirdClause... clauses) {

		Collection<LinkDefinition> defs = new ArrayList<LinkDefinition>();
		
		for (LinkDefinitionGrammar.ThirdClause clause : clauses)
			defs.add(clause.build());
				
		return links(defs);
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
	
	@Override
	public CodelistBuilder attributes(AttributeGrammar.FourthClause... clauses) {
		
		Collection<Attribute> as = new ArrayList<Attribute>();
		
		for (AttributeGrammar.FourthClause clause : clauses)
			as.add(clause.build());
		
		return attributes(as);
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
