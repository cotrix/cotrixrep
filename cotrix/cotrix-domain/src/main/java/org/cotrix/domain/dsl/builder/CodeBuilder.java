package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.dsl.Data;
import org.cotrix.domain.dsl.grammar.AttributeGrammar;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeChangeClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.CodeNewClause;
import org.cotrix.domain.dsl.grammar.CodeGrammar.OptionalClause;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.memory.MCode;

/**
 * Builds {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodeBuilder implements CodeNewClause, CodeChangeClause {

	private final MCode state;
	
	public CodeBuilder(MCode state) {
		this.state = state;
	}
	
	@Override
	public OptionalClause name(QName name) {
		state.qname(name);
		return this;
	}

	
	@Override
	public OptionalClause name(String name) {
		return name(Data.q(name));
	}

	@Override
	public CodeBuilder links(Link... links) {
		
		return links(asList(links));
	}
	
	@Override
	public CodeBuilder links(Collection<Link> links) {
		
		state.links(reveal(links,Link.Private.class));
		
		return this;
	}
	
	@Override
	public CodeBuilder attributes(Attribute ... attributes) {
		return attributes(Arrays.asList(attributes));
	}
	
	@Override
	public CodeBuilder attributes(Collection<Attribute> attributes) {
		
		state.attributes(reveal(attributes,Attribute.Private.class));
		
		return this;
	}
	
	@Override
	public CodeBuilder attributes(AttributeGrammar.OptionalClause... clauses) {
		
		Collection<Attribute> as = new ArrayList<Attribute>();
		
		for (AttributeGrammar.OptionalClause clause : clauses)
			as.add(clause.build());
		
		return attributes(as);
	}

	
	public Code build() {
		return state.entity();
	}
	
}
