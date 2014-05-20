package org.cotrix.parse;

import static java.util.Arrays.*;
import static org.cotrix.TestUtils.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.inject.Inject;

import org.cotrix.io.ParseService;
import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Row;
import org.virtualrepository.tabular.Table;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class Csv2TableTest {

	@Inject
	ParseService parser;
	
	@Test
	public void withDefaults() {
		
		String[][] data = {{"1"}};
		InputStream stream = asCsv(data, Csv2TableDirectives.DEFAULT);
		
		
		
		Table table = parser.parse(stream,Csv2TableDirectives.DEFAULT);
		
		
		
		//all data preserved
		assertEquals(table,data);
		
		//after table is parsed we see one generated column
		assertEquals(1,table.columns().size());
				
	}
	
	@Test
	public void agprod() {
		
		InputStream stream = this.getClass().getResourceAsStream("/ag-prod-cnt.csv");
		
		
		Csv2TableDirectives directives=  new Csv2TableDirectives();
		directives.options().hasHeader(false);
		directives.options().setQuote('"');
		directives.options().setDelimiter(',');
		directives.options().setEncoding(Charset.forName("UTF-8"));
		
		Table table = parser.parse(stream,directives);
		
		System.out.println(table.columns());
		for (Row row : table)
			System.out.println(row);
		
		System.out.println(table.columns());
				
	}
	
	@Test
	public void miniasfis() {
		
		InputStream stream = this.getClass().getResourceAsStream("/ASFIS_MINI.txt");
		
		
		Csv2TableDirectives directives=  new Csv2TableDirectives();
		directives.options().hasHeader(false);
		directives.options().setQuote('"');
		directives.options().setDelimiter('\t');
		directives.options().setEncoding(Charset.forName("ISO-8859-1"));
		
		Table table = parser.parse(stream,directives);
		
		System.out.println(table.columns());
		for (Row row : table)
			System.out.println(row);
		
		System.out.println(table.columns());
				
	}
	
	@Test
	public void withEmbeddedHeaderAndCustomSettings() {
		
		String[][] data = {{"c"},{"1"}};
		
		Csv2TableDirectives directives = new Csv2TableDirectives();
		
		directives.options().setDelimiter('\t');
		directives.options().setQuote('*');
		directives.options().hasHeader(true);
		
		InputStream stream = asCsv(data,directives);
				
		Table table = parser.parse(stream,directives);
		

		assertEquals(asList(new Column("c")),table.columns());

		assertEquals(table,new String[][]{{"1"}});
						
	}

}