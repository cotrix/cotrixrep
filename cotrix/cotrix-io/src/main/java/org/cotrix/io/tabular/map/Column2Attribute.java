package org.cotrix.io.tabular.map;

import static org.cotrix.common.Report.*;
import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.TypeClause;
import org.virtualrepository.tabular.Row;

/**
 * Executes an {@link ColumnDirectives} to map {@link Row}s onto code {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class Column2Attribute {

	private final ColumnDirectives mapping;
	
	/**
	 * Creates an instance with a given mapping.
	 * @param mapping the mapping
	 */
	public Column2Attribute(ColumnDirectives mapping) {
		this.mapping=mapping;
	}
	
	/**
	 * Maps a row onto an attribute.
	 * @param codename the name of the code for this attribute
	 * @param row the row
	 * @return the attribute
	 */
	public Attribute map(String codename, Row row) {
		
		String value = row.get(mapping.column());
		
		if (!valid(codename,value))
			return null;
		
		Attribute attribute = null;
		
		TypeClause sentence = attr().name(mapping.name()).value(value);
		
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
	
	//helper
	private boolean valid(String codename, String value) {
		
		if (value==null || value.isEmpty()) {
			
			String msg = "code "+codename+" has no value for attribute '"+mapping.name()+"'";
			
			switch(mapping.mode()) {
				case STRICT:
					report().logError(msg);break;
				case LOG:
					report().logWarning(msg);break;
			}
			
			return false;
		}
		else
			return true;
	
	}
}

