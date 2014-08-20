package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.dsl.Data;
import org.cotrix.domain.dsl.grammar.AttributeGrammar;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar.LinkDefinitionChangeClause;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar.LinkDefinitionNewClause;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.LinkDefinitionGrammar.ThirdClause;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.links.LinkOfLink;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.memory.MLinkDef;
import org.cotrix.domain.utils.AttributeTemplate;
import org.cotrix.domain.values.ValueFunction;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class LinkDefinitionBuilder  {

	public LinkDefinitionBuilder(MLinkDef state) {
		this.state = state;
	}

	//shared state
	private final MLinkDef state;
	
	//shared clauses
	
	public void name(QName name) {
		state.qname(name);
		
	}
	
	public void name(String name) {
		name(Data.q(name));
	}
	
	
	//new sentence
	
	public class NewClause implements LinkDefinitionNewClause, SecondClause {
		
		
		public NewClause name(QName name) {
			LinkDefinitionBuilder.this.name(name);
			return this;
		}
		
		public NewClause name(String name) {
			LinkDefinitionBuilder.this.name(name);
			return this;
		}
		
		public ThirdClause target(Codelist target) {
			
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
		
		public ThirdClause name(QName name) {
			LinkDefinitionBuilder.this.name(name);
			return this;
		}
		
		public ThirdClause name(String name) {
			LinkDefinitionBuilder.this.name(name);
			return this;
		}

	}
	
	
	public class OptClause implements ThirdClause {

		public ThirdClause anchorTo(Attribute template) {
			state.valueType(new AttributeLink(new AttributeTemplate(template))); 
			return this;
		}
		
		public ThirdClause anchorTo(LinkDefinition template) {
			state.valueType(new LinkOfLink(template));
			return this;
		}
		
		public ThirdClause anchorToName() {
			state.valueType(NameLink.INSTANCE);
			return this;
		}


		public ThirdClause attributes(Attribute ... attributes) {
			attributes(Arrays.asList(attributes));
			return this;
		}
		
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
		public ThirdClause transformWith(ValueFunction function) {
			state.function(function);
			return this;
		}
		
		@Override
		public ThirdClause occurs(Range range) {
			state.range(range);
			return this;
		}

		@Override
		public LinkDefinition build() {
			
			return state.entity();
		}
		
		
		
	}

	
	
	


	
	
	

}
