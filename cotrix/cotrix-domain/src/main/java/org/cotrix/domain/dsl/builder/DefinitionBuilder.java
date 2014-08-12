package org.cotrix.domain.dsl.builder;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.dsl.Entities;
import org.cotrix.domain.dsl.grammar.AttributeDefinitionGrammar.AttributeDefinitionChangeClause;
import org.cotrix.domain.dsl.grammar.AttributeDefinitionGrammar.AttributeDefinitionNewClause;
import org.cotrix.domain.dsl.grammar.AttributeDefinitionGrammar.OptionalClause;
import org.cotrix.domain.memory.MAttrDef;
import org.cotrix.domain.values.ValueType;

/**
 * Builds attribute {@link AttributeDefinition}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class DefinitionBuilder implements AttributeDefinitionNewClause, AttributeDefinitionChangeClause {

	private final MAttrDef state;

	
	public DefinitionBuilder(MAttrDef state) {
		this.state = state;
	}
	
	@Override
	public DefinitionBuilder name(QName name) {
		state.qname(name);
		return this;
	}
	
	@Override
	public DefinitionBuilder name(String name) {
		return name(Entities.q(name));
	}

	@Override
	public DefinitionBuilder is(QName type) {
		state.type(type);
		return this;
	}
	
	@Override
	public DefinitionBuilder is(String type) {
		return is(Entities.q(type));
	}

	@Override
	public DefinitionBuilder valueIs(ValueType valueType) {
		state.valueType(valueType);
		return this;
	}
	
	@Override
	public OptionalClause occurs(Range range) {
		state.range(range);
		return this;
	}
	
	@Override
	public DefinitionBuilder in(String language) {
		state.language(language);
		return this;
	}

	@Override
	public AttributeDefinition build() {
		return state.entity();
	}
}
