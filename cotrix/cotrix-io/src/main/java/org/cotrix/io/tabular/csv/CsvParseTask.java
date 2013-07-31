package org.cotrix.io.tabular.csv;

import java.io.InputStream;

import org.cotrix.io.parse.ParseTask;
import org.virtualrepository.csv.CsvTable;
import org.virtualrepository.tabular.Table;

public class CsvParseTask implements ParseTask<Table,CsvParseDirectives> {

	@Override
	public Table parse(InputStream stream, CsvParseDirectives directives) throws Exception {
		
		//we delegate parsing to virtual repository classes
		return new CsvTable(directives.options(), stream);

	}

	
	@Override
	public Class<? extends CsvParseDirectives> directedBy() {
		return CsvParseDirectives.class;
	}
	
	@Override
	public String toString() {
		return "csv-parser";
	}
}
