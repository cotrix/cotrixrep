package org.cotrix.domain.dsl.builder;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeDeltaClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeStartClause;
import org.cotrix.domain.memory.AttributeMS;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributeTypeBuilder implements AttributeStartClause, AttributeDeltaClause {

	private final AttributeMS state;

	
	public AttributeTypeBuilder(AttributeMS state) {
		this.state = state;
	}
	
	@Override
	public AttributeTypeBuilder name(QName name) {
		state.name(name);
		return this;
	}
	
	@Override
	public AttributeTypeBuilder name(String name) {
		return name(Codes.q(name));
	}

	@Override
	public AttributeTypeBuilder ofType(QName type) {
		state.type(type);
		return this;
	}
	
	@Override
	public AttributeTypeBuilder ofType(String type) {
		return ofType(Codes.q(type));
	}
	
	@Override
	public AttributeTypeBuilder in(String language) {
		state.language(language);
		return this;
	}

	@Override
	public AttributeTypeBuilder value(String value) {
		state.value(value);
		return this;

	}

	@Override
	public Attribute build() {
		return state.entity();
	}
}
