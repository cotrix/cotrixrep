package org.cotrix.io.tabular.map;

import static org.cotrix.common.Report.*;
import static org.cotrix.common.Report.Item.Type.*;
import static org.cotrix.domain.dsl.Data.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.io.utils.SharedDefinitionPool;
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
	public Attribute map(String codename, Row row, SharedDefinitionPool defs) {
		
		String value = row.get(mapping.column());
		
		if (!valid(codename,value))
			return null;
		
		AttributeDefinition def = defs.get(mapping.name(), mapping.type(),mapping.language());
		
		return attribute()
					.instanceOf(def)
					.value(value).build();
	}
	
	//helper
	private boolean valid(String codename, String value) {
		
		if (value==null || value.isEmpty()) {
			
			if (mapping.required()) {
			
				String msg = "code "+codename+" has no value for attribute '"+mapping.name()+"'";
				
				switch(mapping.mode()) {
					case strict:
						report().log(msg).as(ERROR);break;
					case log:
						report().log(msg).as(WARN);break;
					default:
				}
			}
			
			return false;
		}
		else
			return true;
	
	}
}

