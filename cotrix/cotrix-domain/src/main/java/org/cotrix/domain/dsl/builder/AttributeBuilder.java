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
import org.cotrix.domain.memory.AttributeMS;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributeBuilder implements AttributeStartClause, AttributeDeltaClause {

	private final AttributeMS state;

	
	public AttributeBuilder() {
		this.state = new AttributeMS(null);
	}
	
	public AttributeBuilder(String id) {
		this.state = new AttributeMS(id);
		state.type(null);
		state.status(MODIFIED);
		
	}
	
	@Override
	public ValueClause name(QName name) {
		state.name(name);
		return this;
	}
	
	@Override
	public ValueClause name(String name) {
		return name(Codes.q(name));
	}
	
	@Override
	public Attribute delete() {
		state.status(DELETED);
		return build();
	}

	@Override
	public LanguageClause ofType(QName type) {
		state.type(type);
		return this;
	}
	
	@Override
	public LanguageClause ofType(String type) {
		return ofType(Codes.q(type));
	}
	
	@Override
	public BuildClause<Attribute> in(String language) {
		state.language(language);
		return this;
	}

	@Override
	public TypeClause value(String value) {
		state.value(value);
		return this;

	}

	@Override
	public Attribute build() {
		return state.entity();
	}
}
