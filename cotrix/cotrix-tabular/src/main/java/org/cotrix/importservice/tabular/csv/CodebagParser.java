package org.cotrix.importservice.tabular.csv;

import java.io.InputStream;

import org.cotrix.domain.Codebag;
import org.cotrix.importservice.Parser;
import org.cotrix.importservice.tabular.mapping.CodebagMapper;
import org.cotrix.importservice.tabular.model.Table;

//since model it's streamed, combines parser and model
public class CodebagParser implements Parser<Codebag,CSV2Codebag> {

	public Codebag parse(InputStream stream,CSV2Codebag d) {
		
		CodebagMapper mapper = new CodebagMapper(d.mapping());
		
		Table table = new CSVTable(stream,d.options());
		
		return mapper.map(table);
	
	}
	
	@Override
	public Class<CSV2Codebag> directedBy() {
		return CSV2Codebag.class;
	}
}
