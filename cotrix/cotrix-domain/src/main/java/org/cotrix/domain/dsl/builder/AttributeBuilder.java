package org.cotrix.domain.dsl.builder;

import static org.cotrix.domain.trait.Status.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeDeltaClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeStartClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.LanguageClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.TypeClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.ValueClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.po.AttributePO;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributeBuilder implements AttributeStartClause, AttributeDeltaClause {

	private final AttributePO po;

	
	public AttributeBuilder() {
		this.po = new AttributePO(null);
	}
	
	public AttributeBuilder(String id) {
		this.po = new AttributePO(id);
		po.type(null);
		po.status(MODIFIED);
		
	}
	
	@Override
	public ValueClause name(QName name) {
		po.name(name);
		return this;
	}
	
	@Override
	public ValueClause name(String name) {
		return name(Codes.q(name));
	}
	
	@Override
	public Attribute delete() {
		po.status(DELETED);
		return build();
	}

	@Override
	public LanguageClause ofType(QName type) {
		po.type(type);
		return this;
	}
	
	@Override
	public LanguageClause ofType(String type) {
		return ofType(Codes.q(type));
	}
	
	@Override
	public BuildClause<Attribute> in(String language) {
		po.language(language);
		return this;
	}

	@Override
	public TypeClause value(String value) {
		po.value(value);
		return this;

	}

	@Override
	public Attribute build() {
		return po.entity();
	}
}
