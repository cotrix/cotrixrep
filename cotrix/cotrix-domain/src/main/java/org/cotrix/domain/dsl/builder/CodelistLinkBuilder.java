package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.dsl.builder.BuilderUtils.*;

import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkNewClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.OptionalClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.LinkOfLink;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.links.OccurrenceRange;
import org.cotrix.domain.links.ValueFunction;
import org.cotrix.domain.memory.CodelistLinkMS;
import org.cotrix.domain.utils.AttributeTemplate;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkBuilder  {

	public CodelistLinkBuilder(CodelistLinkMS state) {
		this.state = state;
	}

	//shared state
	private final CodelistLinkMS state;
	
	//shared clauses
	
	public void name(QName name) {
		state.name(name);
		
	}
	
	public void name(String name) {
		name(Codes.q(name));
	}
	
	
	//new sentence
	
	public class NewClause implements CodelistLinkNewClause, LinkTargetClause<Codelist,OptionalClause> {
		
		
		public NewClause name(QName name) {
			CodelistLinkBuilder.this.name(name);
			return this;
		}
		
		public NewClause name(String name) {
			CodelistLinkBuilder.this.name(name);
			return this;
		}
		
		public OptionalClause target(Codelist target) {
			
			notNull("codelist",target);

			state.target(reveal(target,Codelist.Private.class));
			
			return CodelistLinkBuilder.this.new OptClause();
		}
	}
	
	
	//change sentence
	
	public class ChangeClause extends OptClause implements CodelistLinkChangeClause {
		
		public OptionalClause name(QName name) {
			CodelistLinkBuilder.this.name(name);
			return this;
		}
		
		public OptionalClause name(String name) {
			CodelistLinkBuilder.this.name(name);
			return this;
		}

	}
	
	
	public class OptClause implements OptionalClause {

		public OptionalClause anchorTo(Attribute template) {
			state.valueType(new AttributeLink(new AttributeTemplate(template))); 
			return this;
		}
		
		public OptionalClause anchorTo(CodelistLink template) {
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
		
		public OptionalClause attributes(List<Attribute> attributes) {
			state.attributes(reveal(attributes,Attribute.Private.class));
			return this;
		}
		
		@Override
		public OptionalClause transformWith(ValueFunction function) {
			state.function(function);
			return this;
		}
		
		@Override
		public OptionalClause occurs(OccurrenceRange range) {
			state.range(range);
			return this;
		}

		@Override
		public CodelistLink build() {
			
			return state.entity();
		}
		
		
		
	}

	
	
	


	
	
	

}
