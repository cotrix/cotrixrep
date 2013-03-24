package org.cotrix.importservice.tabular.mapping;

import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.ThirdClause;
import org.cotrix.importservice.tabular.mapping.AttributeMapping.Mode;
import org.cotrix.importservice.tabular.model.Row;

/**
 * Executes an {@link AttributeMapping} to map {@link Row}s onto code {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
class AttributeMapper {

	private final AttributeMapping mapping;
	
	/**
	 * Creates an instance with a given mapping.
	 * @param mapping the mapping
	 */
	public AttributeMapper(AttributeMapping mapping) {
		this.mapping=mapping;
	}
	
	/**
	 * Maps a row onto an attribute.
	 * @param row the row
	 * @return the attribute
	 */
	Attribute map(Row row) {
		
		String value = row.get(mapping.column());
		
		if (value==null || value.isEmpty())
			if (mapping.mode()==Mode.STRICT)
				throw new IllegalArgumentException("missing value for attribute '"+mapping.name()+"' in mode '"+mapping.mode()+"'");
			else
				return null;
		
		Attribute attribute = null;
		
		ThirdClause sentence = attr().name(mapping.name()).value(value);
		
		if (mapping.type()!=null)
			if (mapping.language()!=null)
				attribute = sentence.ofType(mapping.type()).in(mapping.language()).build();
			else
				attribute = sentence.ofType(mapping.type()).build();
		else
			if (mapping.language()!=null)
				attribute = sentence.in(mapping.language()).build();
			else
				attribute = sentence.build();
		
		return attribute;
	}
}

