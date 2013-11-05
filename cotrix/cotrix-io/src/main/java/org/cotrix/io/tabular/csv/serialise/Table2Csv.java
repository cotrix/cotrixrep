package org.cotrix.io.tabular.csv.serialise;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.cotrix.io.impl.SerialisationTask;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

import au.com.bytecode.opencsv.CSVWriter;

public class Table2Csv implements SerialisationTask<Table,Table2CsvDirectives> {

	@Override
	public Class<Table2CsvDirectives> directedBy() {
		return Table2CsvDirectives.class;
	}
	
	@Override
	public void serialise(Table table, OutputStream stream, Table2CsvDirectives directives) throws Exception {
		

		CsvCodelist opts = directives.options();
		
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(stream), opts.delimiter(),opts.quote());
		
		List<String> values = new ArrayList<String>();
		
		for (Row row : table) {
			values.clear();
			for (Column column : table.columns())
				values.add(row.get(column));
			writer.writeNext(values.toArray(new String[0]));
		}
		
		writer.flush();
		writer.close();
	}
	
	@Override
	public String toString() {
		return "table-2-csv";
	}
}
