package org.cotrix.domain.dsl.builder;

import static org.cotrix.domain.dsl.Codes.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.CommonDefinition;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeChangeClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.AttributeNewClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.OptionalClause;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.ValueClause;
import org.cotrix.domain.memory.AttributeMS;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributeBuilder implements AttributeNewClause, AttributeChangeClause, ValueClause {

	private final AttributeMS state;

	
	public AttributeBuilder(AttributeMS state) {
		this.state = state;
	}
	
	@Override
	public ValueClause instanceOf(AttributeDefinition def) {
		
		state.definition(reveal(def).bean());
		
		return this;
	}
	
	@Override
	public ValueClause instanceOf(CommonDefinition def) {
		state.definition(def.state());
		return this;
	}
	
	@Override
	public AttributeBuilder name(QName name) {
		state.qname(name);
		return this;
	}
	
	@Override
	public AttributeBuilder name(String name) {
		return name(Codes.q(name));
	}

	@Override
	public AttributeBuilder ofType(QName type) {
		state.type(type);
		return this;
	}
	
	@Override
	public AttributeBuilder ofType(String type) {
		return ofType(Codes.q(type));
	}
	
	@Override
	public AttributeBuilder in(String language) {
		state.language(language);
		return this;
	}

	@Override
	public AttributeBuilder value(String value) {
		state.value(value);
		return this;

	}
	
	@Override
	public OptionalClause description(String description) {
		state.description(description);
		return this;
	}

	@Override
	public Attribute build() {
		return state.entity();
	}
}
