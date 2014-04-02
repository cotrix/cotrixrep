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
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.memory.CodelistLinkMS;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkBuilder implements CodelistLinkNewClause, CodelistLinkChangeClause {

	
	private final CodelistLinkMS state;
	
	public CodelistLinkBuilder(CodelistLinkMS state) {
		this.state = state;
	}
	
	@Override
	public CodelistLinkBuilder name(QName name) {
		state.name(name);
		return this;
	}
	
	@Override
	public CodelistLinkBuilder name(String name) {
		return name(Codes.q(name));
	}
	
	@Override
	public OptionalClause onAttribute(Attribute template) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public OptionalClause onLink(CodelistLink template) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public OptionalClause onName() {
		state.type(NameLink.INSTANCE);
		return this;
	}
	
	@Override
	public CodelistLinkBuilder attributes(Attribute ... attributes) {
		return attributes(Arrays.asList(attributes));
	}
	
	@Override
	public CodelistLinkBuilder attributes(List<Attribute> attributes) {
		state.attributes(reveal(attributes,Attribute.Private.class));
		return this;
	}
	
	@Override
	public OptionalClause target(Codelist target) {
		
		notNull("codelist",target);

		if (target.id()==null)
			throw new IllegalArgumentException("cannot link to an unidentified codelist");
		
		state.target(reveal(target,Codelist.Private.class));
		
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
