package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.trait.Status.*;

import java.util.ArrayList;
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

	
	private final CodelistLinkMS po;
	
	public CodelistLinkBuilder() {
		this.po = new CodelistLinkMS(null);
	}
	
	public CodelistLinkBuilder(String id) {
		this.po = new CodelistLinkMS(id);
		po.status(MODIFIED);
	}
	
	@Override
	public SecondClause name(QName name) {
		po.name(name);
		return this;
	}
	
	@Override
	public SecondClause name(String name) {
		return name(Codes.q(name));
	}
	
	@Override
	public CodelistLink delete() {
		po.status(DELETED);
		return build();
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		return attributes(Arrays.asList(attributes));
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {
		
		List<Attribute.State> state = new ArrayList<Attribute.State>();
		
		for (Attribute a : attributes)
			state.add(reveal(a,Attribute.Private.class).state());
		
		po.attributes(state);
		
		return this;
	}
	
	@Override
	public FinalClause target(Codelist target) {
		
		notNull("codelist",target);

		if (target.id()==null)
			throw new IllegalArgumentException("cannot link to an unidentified codelist");
		
		po.targetId(target.id());
		
		return this;
	}
	
	@Override
	public CodelistLink build() {
		return po.entity();
	}
	

}
