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
import org.cotrix.domain.links.NameLink;
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
			state.type(new AttributeLink(new AttributeTemplate(template))); 
			return this;
		}
		
		public OptionalClause anchorTo(CodelistLink template) {
			throw new UnsupportedOperationException();
		}
		
		public OptionalClause anchorToName() {
			state.type(NameLink.INSTANCE);
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
		public CodelistLink build() {
			
			//cannot capture 'by-flow' that two fields are mandatory @ create time, but independent at modify time
			//so we allow the latter, and check explicitly for the former
			if (state.status()==null) {
				
				if (state.target()==null)
					throw new IllegalStateException("no target specified for codelist link "+state.name());
			
				if (state.type()==null)
					throw new IllegalStateException("no type specified for codelist link "+state.name());
			}	
		
			return state.entity();
		}
		
		
		
	}

	
	
	


	
	
	

}
