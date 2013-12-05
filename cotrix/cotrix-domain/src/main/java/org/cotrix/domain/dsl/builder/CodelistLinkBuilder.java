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
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.SecondClause;
import org.cotrix.domain.memory.CodelistLinkMS;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkBuilder implements CodelistLinkNewClause, CodelistLinkChangeClause, FinalClause {

	
	private final CodelistLinkMS state;
	
	public CodelistLinkBuilder(CodelistLinkMS state) {
		this.state = state;
	}
	
	@Override
	public SecondClause name(QName name) {
		state.name(name);
		return this;
	}
	
	@Override
	public SecondClause name(String name) {
		return name(Codes.q(name));
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		return attributes(Arrays.asList(attributes));
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {
		state.attributes(reveal(attributes,Attribute.Private.class));
		return this;
	}
	
	@Override
	public FinalClause target(Codelist target) {
		
		notNull("codelist",target);

		if (target.id()==null)
			throw new IllegalArgumentException("cannot link to an unidentified codelist");
		
		state.targetId(target.id());
		
		return this;
	}
	
	@Override
	public CodelistLink build() {
		return state.entity();
	}
	

}
