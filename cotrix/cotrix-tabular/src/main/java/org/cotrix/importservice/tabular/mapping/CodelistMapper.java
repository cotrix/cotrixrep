package org.cotrix.importservice.tabular.mapping;

import static org.cotrix.domain.dsl.Codes.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.importservice.tabular.model.Row;
import org.cotrix.importservice.tabular.model.Table;

/**
 * Executes a {@link CodelistMapping} to map {@link Table}s onto {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodelistMapper {

	private final List<Code> codes = new ArrayList<Code>();
	private final List<AttributeMapper> attributeMappers = new ArrayList<AttributeMapper>();
	private final CodelistMapping mapping;
	
	/**
	 * Creates an instance with a given mapping.
	 * @param mapping the mapping
	 */
	public CodelistMapper(CodelistMapping mapping) {
		
		this.mapping=mapping;
		
		for (AttributeMapping attributeMapping : mapping.attributeMappings()) 
			attributeMappers.add(new AttributeMapper(attributeMapping));
	}
	
	/**
	 * Maps a table on a codelist.
	 * @param table the table
	 * @return the codelist
	 */
	public Codelist map(Table table) {
		
		for (Row row : table)
			map(row);
		
		return list();
	}
	
	/**
	 * Executes the part of the mapping that pertains on a single row.
	 * <p>
	 * Exposed for collaboration with a {@link CodebagMapper}.
	 * 
	 * @param row the row.
	 */
	void map(Row row) {
		
		List<Attribute> attributes = new ArrayList<Attribute>();
		
		String name = row.get(mapping.column());
		
		if (name==null || name.isEmpty())
			throw new IllegalArgumentException("found a null code in "+mapping.name()+" in STRICT mode");
		
		for (AttributeMapper attributeMapper : attributeMappers) {
			Attribute parsed = attributeMapper.map(row);
			if (parsed != null)
				attributes.add(parsed);
		}
		
		Code code = code().name(name).attributes(attributes.toArray(new Attribute[0])).build();
		
		codes.add(code);
		
	}
	
	/**
	 * Returns the mapped codelist.
	 * <p>
	 * Exposed for collaboration with a {@link CodebagMapper}.
	 * @return the mapped codelist
	 */
	Codelist list() {
		return codelist().
				name(mapping.name())
				.with(codes.toArray(new Code[0]))
				.attributes(mapping.attributes().toArray(new Attribute[0])).build();
	}
}

