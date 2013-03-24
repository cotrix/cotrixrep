package org.cotrix.importservice.tabular.mapping;

import static org.cotrix.domain.dsl.Codes.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.importservice.tabular.model.Row;
import org.cotrix.importservice.tabular.model.Table;

/**
 * Executes a {@link CodebagMapping} to map {@link Table}s onto {@link Codebag}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class CodebagMapper {

	private final CodebagMapping mapping;
	private final List<CodelistMapper> mappers = new ArrayList<CodelistMapper>();
	
	/**
	 * Creates an instance with a given mapping.
	 * @param mapping the mapping
	 */
	public CodebagMapper(CodebagMapping mapping) {
		
		this.mapping=mapping;
		
		//creates mappers from mappings
		for (CodelistMapping listMapping : mapping.codelistMappings()) 
			mappers.add(new CodelistMapper(listMapping));
	}
	
	/**
	 * Maps a table on a codebag.
	 * @param table the table
	 * @return the codebag
	 */
	public Codebag map(Table table) {
		
		//delegate to list mappers, row-by-row
		for (Row row : table)
			for (CodelistMapper mapper : mappers)
				mapper.map(row);
		
		
		//collect lists after parsing
		List<Codelist> lists = new ArrayList<Codelist>();
		for (CodelistMapper mapper : mappers)
			lists.add(mapper.list());
		
		
		//assemble and return codebag
		Codelist[] listsArray = lists.toArray(new Codelist[0]);
		Attribute[] attributes = mapping.attributes().toArray(new Attribute[0]);
		
		return codebag().name(mapping.name())
						.with(listsArray)
						.attributes(attributes)
						.build();
	}
	
	
}

