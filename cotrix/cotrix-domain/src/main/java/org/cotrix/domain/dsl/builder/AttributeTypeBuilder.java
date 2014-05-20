package org.cotrix.domain.dsl.builder;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeType;
import org.cotrix.domain.attributes.AttributeValueType;
import org.cotrix.domain.common.OccurrenceRange;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.AttributeTypeGrammar.AttributeTypeChangeClause;
import org.cotrix.domain.dsl.grammar.AttributeTypeGrammar.AttributeTypeNewClause;
import org.cotrix.domain.dsl.grammar.AttributeTypeGrammar.OptionalClause;
import org.cotrix.domain.memory.AttributeTypeMS;

/**
 * Builds {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributeTypeBuilder implements AttributeTypeNewClause, AttributeTypeChangeClause {

	private final AttributeTypeMS state;

	
	public AttributeTypeBuilder(AttributeTypeMS state) {
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
	public AttributeTypeBuilder valuesIs(AttributeValueType valueType) {
		state.valueType(valueType);
		return this;
	}
	
	@Override
	public OptionalClause occurs(OccurrenceRange range) {
		state.range(range);
		return this;
	}
	
	@Override
	public AttributeTypeBuilder in(String language) {
		state.language(language);
		return this;
	}

	@Override
	public AttributeType build() {
		return state.entity();
	}
}
