package org.cotrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import org.cotrix.importservice.tabular.csv.CSVOptions;

import au.com.bytecode.opencsv.CSVWriter;

public class TestUtils {

	public static File toCSVFile(String[][] data) {
		return toCSVFile(data,CSVOptions.defaultDelimiter);
	}
	
	public static InputStream asCSVStream(String[][] data) {
		return asCSVStream(data,CSVOptions.defaultDelimiter);
	}
	
	public static InputStream asCSVStream(String[][] data, char delimiter) {
		
		try {
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			CSVWriter writer = new CSVWriter(new OutputStreamWriter(out), delimiter);
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

	public static File toCSVFile(String[][] data, char delimiter) {
		
		try {
			File file = new File("src/test/resources/test.csv");
			CSVWriter writer = new CSVWriter(new FileWriter(file),delimiter);
			for (String[] row : data)
				writer.writeNext(row);
			writer.flush();
			writer.close();
			return file;
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
}
