package org.cotrix;

import static java.util.Arrays.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.junit.Assert;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.DefaultTable;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

import au.com.bytecode.opencsv.CSVWriter;

public class TestUtils {

	public static InputStream asCsv(String[][] data, Csv2TableDirectives directives) {
		
		CsvCodelist opts =directives.options();
				
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			CSVWriter writer = new CSVWriter(new OutputStreamWriter(out, opts.encoding()), opts.delimiter(), opts.quote());
			for (String[] row : data)
				writer.writeNext(row);
			writer.flush();
			writer.close();
			return new ByteArrayInputStream(out.toByteArray());
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Table asTable(String[][] data, String... cols) {

		Column[] columns = columns(cols);
		List<Row> rows = new ArrayList<Row>();
		for (String[] row : data) {
			Map<QName, String> map = new HashMap<QName, String>();
			for (int i = 0; i < row.length; i++)
				map.put(columns[i].name(), row[i]);
			rows.add(new Row(map));
		}

		return new DefaultTable(asList(columns), rows);
	}
	
	public static String serialise(Table table) {
		
		StringBuilder builder = new StringBuilder(table.toString()+"\n");
		for (Row r : table)
			builder.append(r);
		
		return builder.toString();
	}
	public static void assertEquals(Table table1,Table table2) {
		
		Assert.assertEquals(table1.toString(), table2.toString());
		
	}
	
	public static void assertEquals(Table table,String[][] data) {
		
		int i = 0;
		for (Row row : table) {
			List<String> vals = new ArrayList<String>();
			for (Column column : table.columns())
				vals.add(row.get(column));
			Assert.assertEquals(vals,asList(data[i]));
			i++;
		}
		
		Assert.assertEquals(i,data.length);
	}

	public static Column[] columns(String... names) {
		List<Column> list = new ArrayList<Column>();
		for (String name : names)
			list.add(new Column(name));
		return list.toArray(new Column[0]);
	}
}
