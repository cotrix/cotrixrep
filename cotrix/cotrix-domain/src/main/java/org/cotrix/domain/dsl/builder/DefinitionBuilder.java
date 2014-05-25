package org.cotrix.domain.dsl.builder;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.dsl.Codes;
import org.cotrix.domain.dsl.grammar.DefinitionGrammar.DefinitionChangeClause;
import org.cotrix.domain.dsl.grammar.DefinitionGrammar.DefinitionNewClause;
import org.cotrix.domain.dsl.grammar.DefinitionGrammar.OptionalClause;
import org.cotrix.domain.memory.DefinitionMS;
import org.cotrix.domain.values.ValueType;

/**
 * Builds attribute {@link Definition}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class DefinitionBuilder implements DefinitionNewClause, DefinitionChangeClause {

	private final DefinitionMS state;

	
	public DefinitionBuilder(DefinitionMS state) {
		this.state = state;
	}
	
	@Override
	public DefinitionBuilder name(QName name) {
		state.name(name);
		return this;
	}
	
	@Override
	public DefinitionBuilder name(String name) {
		return name(Codes.q(name));
	}

	@Override
	public DefinitionBuilder is(QName type) {
		state.type(type);
		return this;
	}
	
	@Override
	public DefinitionBuilder is(String type) {
		return is(Codes.q(type));
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
	public Definition build() {
		return state.entity();
	}
}
