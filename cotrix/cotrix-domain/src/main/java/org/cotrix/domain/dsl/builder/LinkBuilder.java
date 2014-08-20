package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.dsl.grammar.AttributeGrammar;
import org.cotrix.domain.dsl.grammar.LinkGrammar.CodelinkChangeClause;
import org.cotrix.domain.dsl.grammar.LinkGrammar.CodelinkNewClause;
import org.cotrix.domain.dsl.grammar.LinkGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.LinkGrammar.ThirdClause;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.memory.MLink;


public class LinkBuilder implements ThirdClause, SecondClause, CodelinkChangeClause {

	
	private final MLink state;
	
	public LinkBuilder(MLink state) {
		this.state = state;
	}
	
	@Override
	public ThirdClause target(Code code) {
		
		notNull("code",code);
		
		Code.Private privatecode = reveal(code);
		
		if (privatecode.isChangeset())
			throw new IllegalArgumentException("invalid link: target code cannot be a changeset");
		
		Code.Bean target = privatecode.bean();
		
		state.target(target);
		
		return LinkBuilder.this;
	}
	
	@Override
	public ThirdClause attributes(Attribute ... attributes) {
		return attributes(asList(attributes));
	}
	
	@Override
	public ThirdClause attributes(Collection<Attribute> attributes) {
		state.attributes(reveal(attributes,Attribute.Private.class));
		return this;
	}
	
	@Override
	public ThirdClause attributes(AttributeGrammar.FourthClause... clauses) {
		
		Collection<Attribute> as = new ArrayList<Attribute>();
		
		for (AttributeGrammar.FourthClause clause : clauses)
			as.add(clause.build());
		
		return attributes(as);
	}
	
	@Override
	public Link build() {
		return state.entity();
	}
	
	
	//new sentence
	
	public class NewClause implements CodelinkNewClause {
		
		@Override
		public SecondClause instanceOf(LinkDefinition linktype) {
			
			LinkDefinition.Private type = reveal(linktype);
			
			if (type.isChangeset())
				throw new IllegalArgumentException("invalid link: link type cannot be a changeset");
			
			state.definition(type.bean());
			
			return LinkBuilder.this;
		}
	}
	

}
