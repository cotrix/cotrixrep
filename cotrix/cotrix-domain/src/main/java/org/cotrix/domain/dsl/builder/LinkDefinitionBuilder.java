package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar.LinkDefinitionChangeClause;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar.LinkDefinitionNewClause;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar.OptionalClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.links.LinkOfLink;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.memory.LinkDefinitionMS;
import org.cotrix.domain.utils.AttributeTemplate;
import org.cotrix.domain.values.ValueFunction;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class LinkDefinitionBuilder  {

	public LinkDefinitionBuilder(LinkDefinitionMS state) {
		this.state = state;
	}

	//shared state
	private final LinkDefinitionMS state;
	
	//shared clauses
	
	public void name(QName name) {
		state.qname(name);
		
	}
	
	public void name(String name) {
		name(Codes.q(name));
	}
	
	
	//new sentence
	
	public class NewClause implements LinkDefinitionNewClause, LinkTargetClause<Codelist,OptionalClause> {
		
		
		public NewClause name(QName name) {
			LinkDefinitionBuilder.this.name(name);
			return this;
		}
		
		public NewClause name(String name) {
			LinkDefinitionBuilder.this.name(name);
			return this;
		}
		
		public OptionalClause target(Codelist target) {
			
			notNull("codelist",target);
			
			Codelist.Private list = reveal(target);

			if (list.isChangeset())
				throw new IllegalArgumentException("invalid link: target codelist cannot be a changeset");
			
			state.target(list.bean());
			
			return LinkDefinitionBuilder.this.new OptClause();
		}
	}
	
	
	//change sentence
	
	public class ChangeClause extends OptClause implements LinkDefinitionChangeClause {
		
		public OptionalClause name(QName name) {
			LinkDefinitionBuilder.this.name(name);
			return this;
		}
		
		public OptionalClause name(String name) {
			LinkDefinitionBuilder.this.name(name);
			return this;
		}

	}
	
	
	public class OptClause implements OptionalClause {

		public OptionalClause anchorTo(Attribute template) {
			state.valueType(new AttributeLink(new AttributeTemplate(template))); 
			return this;
		}
		
		public OptionalClause anchorTo(LinkDefinition template) {
			state.valueType(new LinkOfLink(template));
			return this;
		}
		
		public OptionalClause anchorToName() {
			state.valueType(NameLink.INSTANCE);
			return this;
		}


		public OptionalClause attributes(Attribute ... attributes) {
			attributes(Arrays.asList(attributes));
			return this;
		}
		
		public OptionalClause attributes(Collection<Attribute> attributes) {
			state.attributes(reveal(attributes,Attribute.Private.class));
			return this;
		}
		
		@Override
		public OptionalClause transformWith(ValueFunction function) {
			state.function(function);
			return this;
		}
		
		@Override
		public OptionalClause occurs(Range range) {
			state.range(range);
			return this;
		}

		@Override
		public LinkDefinition build() {
			
			return state.entity();
		}
		
		
		
	}

	
	
	


	
	
	

}
