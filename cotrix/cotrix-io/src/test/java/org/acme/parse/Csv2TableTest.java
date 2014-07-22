package org.acme.parse;

import static java.util.Arrays.*;
import static org.acme.TestUtils.*;
import static org.junit.Assert.*;

import java.io.InputStream;

import javax.inject.Inject;

import org.cotrix.io.ParseService;
import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtualrepository.tabular.Column;
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
		assertEquals(data,table);
		
		//after table is parsed we see one generated column
		assertEquals(1,table.columns().size());
				
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

		assertEquals(new String[][]{{"1"}},table);
						
	}

}