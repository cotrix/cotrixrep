package org.cotrix.domain.dsl.builder;

import static org.cotrix.common.Utils.*;

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
public class AttributeBuilder implements AttributeStartClause, AttributeDeltaClause {

	private final AttributeMS state;

	
	public AttributeBuilder(AttributeMS state) {
		this.state = state;
	}
	
	@Override
	public AttributeBuilder name(QName name) {
		state.name(name);
		return this;
	}
	
	@Override
	public AttributeBuilder name(String name) {
		return name(Codes.q(name));
	}

	@Override
	public AttributeBuilder ofType(QName type) {
		notNull("attribute type",type);
		state.type(type);
		return this;
	}
	
	@Override
	public AttributeBuilder ofType(String type) {
		notNull("attribute type",type);
		return ofType(Codes.q(type));
	}
	
	@Override
	public AttributeBuilder in(String language) {
		notNull("attribute language",language);
		state.language(language);
		return this;
	}

	@Override
	public AttributeBuilder value(String value) {
		notNull("attribute value",value);
		state.value(value);
		return this;

	}

	@Override
	public Attribute build() {
		return state.entity();
	}
}
