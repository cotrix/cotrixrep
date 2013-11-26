package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.Utils.*;
import static org.cotrix.domain.trait.Status.*;

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
import org.cotrix.domain.po.CodelistLinkPO;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistLinkBuilder implements CodelistLinkNewClause, CodelistLinkChangeClause, FinalClause {

	
	private final CodelistLinkPO po;
	
	public CodelistLinkBuilder() {
		this.po = new CodelistLinkPO(null);
	}
	
	public CodelistLinkBuilder(String id) {
		this.po = new CodelistLinkPO(id);
		po.setChange(MODIFIED);
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
		po.setChange(DELETED);
		return build();
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
	
	@Override
	public CodelistLink build() {
		return new CodelistLink.Private(po);
	}
	

}
