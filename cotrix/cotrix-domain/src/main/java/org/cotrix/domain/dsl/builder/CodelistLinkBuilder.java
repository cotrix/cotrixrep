package org.cotrix.domain.dsl.builder;

import static org.cotrix.domain.trait.Change.*;
import static org.cotrix.domain.utils.Utils.*;

import java.util.Arrays;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkStartClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.po.CodelistLinkPO;
import org.cotrix.domain.trait.Change;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkBuilder implements CodelistLinkStartClause,SecondClause,FinalClause {

	
	private final CodelistLinkPO po;
	
	
	public CodelistLinkBuilder(String id) {
		this.po = new CodelistLinkPO(id);
	}
	
	@Override
	public SecondClause name(QName name) {
		po.setName(name);
		return this;
	}
	
	@Override
	public SecondClause name(String name) {
		return name(Codes.q(name));
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		po.setAttributes(Arrays.asList(attributes));
		return this;
	}
	
	@Override
	public FinalClause target(Codelist target) {
		
		notNull(target);

		if (target.id()==null)
			throw new IllegalArgumentException("cannot link to an unidentified codelist");
		
		po.setTargetId(target.id());
		
		return this;
	}

	@Override
	public BuildClause<CodelistLink> as(Change change) {
		if (change!=NEW && po.id()==null)
			throw new IllegalStateException("object is marked as update but has its identifier is null");
		po.setChange(change);
		return this;
	}
	
	@Override
	public CodelistLink build() {
		return new CodelistLink.Private(po);
	}
	

}
