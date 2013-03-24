package org.cotrix.importservice.tabular.csv;

import java.io.InputStream;

import org.cotrix.domain.Codelist;
import org.cotrix.importservice.Parser;
import org.cotrix.importservice.tabular.mapping.CodelistMapper;
import org.cotrix.importservice.tabular.model.Table;

//since model it's streamed, combines parser and model
public class CodelistParser implements Parser<Codelist,CSV2Codelist> {

	public Codelist parse(InputStream stream,CSV2Codelist description) {
		
		CodelistMapper mapper = new CodelistMapper(description.mapping());
		
		Table table = new CSVTable(stream,description.options());
		
		return mapper.map(table);
	
	}
	
	@Override
	public Class<CSV2Codelist> directedBy() {
		return CSV2Codelist.class;
	}
	
}
