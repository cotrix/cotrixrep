package org.cotrix.importservice.tabular.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.cotrix.importservice.tabular.model.Row;
import org.cotrix.importservice.tabular.model.Table;

import au.com.bytecode.opencsv.CSVReader;

/**
 * A streaming implementation of {@link Table} of CSV data.
 * @author Fabio Simeoni
 *
 */
public class CSVTable implements Table {

	private final Map<String,String> data = new HashMap<String,String>();
	private List<String> columns; 
	
	private RowIterator iterator;
	
	/**
	 * Creates an instance over a stream a CSV data and with given parsing options. 
	 * @param stream the data
	 * @param options the options
	 */
	public CSVTable(InputStream stream, CSVOptions options) {
		
		CSVReader reader = new CSVReader(new InputStreamReader(stream),options.delimiter());
		
		this.columns = options.columns()==null?readColumns(reader):options.columns();
		
		this.iterator=new RowIterator(reader);
	}
	
	@Override
	public Iterator<Row> iterator() {
		return iterator;
	}
	
	@Override
	public List<String> columns() {
		return columns;
	}
	
	
	//helper: parse columns from data headers 
	private List<String> readColumns(CSVReader reader) {
		
		try {
			String[] headers = reader.readNext();
			return Arrays.asList(headers);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("cannot find column names in the CSV data");
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Row row : this)
			builder.append(row+"\n");
		return builder.toString();
	}
	
	//iterates over rows pulling them from the reader
	class RowIterator implements Iterator<Row> {
		
		private final CSVReader reader;
		private String[] row;
		private Throwable error;
		
		public RowIterator(CSVReader reader) {
			this.reader=reader;
		}
		
		public boolean hasNext() {
			
			try {
				row=reader.readNext();
			}
			catch(IOException e) {
				error=e;
			}
			return row!=null;
		}
		
		public Row next() {
			
			checkRow();
			
			Row result = buildRow();
			
			row=null;
			
			return result;
		}
		
		
		//helper
		private void checkRow() {
			
			if (error!=null)
				throw new RuntimeException(error);
			
			if (row==null && !this.hasNext()) //reads ahead
				throw new NoSuchElementException();
			
			if (row.length>columns.size())
				throw new RuntimeException("invalid CSV data: row "+row+" has more columns than expected ("+columns+")");
		}
		//helper
		private Row buildRow() {
			
			data.clear();
			
			for (int i=0;i<row.length;i++)
				data.put(columns.get(i),row[i]);
			
			return new Row(data);
		}
		
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
}
