package org.cotrix.io.tabular.csv.parse;

import java.io.InputStream;

import org.cotrix.io.impl.ParseTask;
import org.virtualrepository.csv.CsvTable;
import org.virtualrepository.tabular.Table;

/**
 * A {@link ParseTask} for CSV streams.
 * 
 * @author Fabio Simeoni
 *
 */
public class Csv2Table implements ParseTask<Table,Csv2TableDirectives> {

	@Override
	public Table parse(InputStream stream, Csv2TableDirectives directives) throws Exception {
		
		//we delegate parsing to virtual repository classes
		return new CsvTable(directives.options(), stream);

	}

	@Override
	public Class<Csv2TableDirectives> directedBy() {
		return Csv2TableDirectives.class;
	}
	
	@Override
	public String toString() {
		return "csv-parser";
	}
}
