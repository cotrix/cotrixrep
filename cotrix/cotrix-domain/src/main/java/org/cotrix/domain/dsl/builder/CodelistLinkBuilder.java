package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.trait.Change.*;

import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.ChangeClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.CodelistLinkStartClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.FinalClause;
import org.cotrix.domain.dsl.grammar.CodelistLinkGrammar.SecondClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.DeltaClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.po.CodelistLinkPO;
import org.cotrix.domain.trait.Change;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkBuilder implements CodelistLinkStartClause,SecondClause,FinalClause, DeltaClause<NameClause<SecondClause>, ChangeClause, CodelistLink>, ChangeClause {

	
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
	public CodelistLink delete() {
		return this.as(DELETED).build();
	}
	
	
	@Override
	public NameClause<SecondClause> add() {
		return this.as(NEW);
	}
	
	@Override
	public ChangeClause modify() {
		return this.as(MODIFIED);
	}
	
	@Override
	public FinalClause attributes(Attribute ... attributes) {
		po.setAttributes(Arrays.asList(attributes));
		return this;
	}
	
	@Override
	public FinalClause attributes(List<Attribute> attributes) {
		po.setAttributes(attributes);
		return this;
	}
	
	@Override
	public FinalClause target(Codelist target) {
		
		notNull("codelist",target);

		if (target.id()==null)
			throw new IllegalArgumentException("cannot link to an unidentified codelist");
		
		po.setTargetId(target.id());
		
		return this;
	}

	private CodelistLinkBuilder as(Change change) {
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
