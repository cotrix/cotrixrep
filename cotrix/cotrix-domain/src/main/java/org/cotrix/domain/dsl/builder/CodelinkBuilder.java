package org.cotrix.domain.dsl.builder;

import static java.util.Arrays.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;

import java.util.Collection;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.CodelinkChangeClause;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.CodelinkNewClause;
import org.cotrix.domain.dsl.grammar.CodelinkGrammar.OptionalClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.memory.CodelinkMS;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelinkBuilder implements OptionalClause, LinkTargetClause<Code,OptionalClause>, CodelinkChangeClause {

	
	private final CodelinkMS state;
	
	public CodelinkBuilder(CodelinkMS state) {
		this.state = state;
	}
	
	@Override
	public OptionalClause target(Code code) {
		
		notNull("code",code);
		
		Code.Private privatecode = reveal(code);
		
		if (privatecode.isChangeset())
			throw new IllegalArgumentException("invalid link: target code cannot be a changeset");
		
		Code.State target = privatecode.state();
		
		state.target(target);
		
		return CodelinkBuilder.this;
	}
	
	@Override
	public OptionalClause attributes(Attribute ... attributes) {
		return attributes(asList(attributes));
	}
	
	@Override
	public OptionalClause attributes(Collection<Attribute> attributes) {
		state.attributes(reveal(attributes,Attribute.Private.class));
		return this;
	}
	
	
	@Override
	public Codelink build() {
		return state.entity();
	}
	
	
	//new sentence
	
	public class NewClause implements CodelinkNewClause {
		
		@Override
		public LinkTargetClause<Code,OptionalClause> instanceOf(LinkDefinition linktype) {
			
			LinkDefinition.Private type = reveal(linktype);
			
			if (type.isChangeset())
				throw new IllegalArgumentException("invalid link: link type cannot be a changeset");
			
			state.definition(type.state());
			
			return CodelinkBuilder.this;
		}
	}
	

}
