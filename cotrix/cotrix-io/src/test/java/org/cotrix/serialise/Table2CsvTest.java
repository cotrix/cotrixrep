package org.cotrix.serialise;

import static org.cotrix.TestUtils.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import org.cotrix.io.ParseService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.cotrix.io.tabular.csv.serialise.Table2CsvDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class Table2CsvTest {

	@Inject
	SerialisationService serialiser;

	@Inject
	ParseService parser;
	
	@Test
	public void serialiseWithDefaults() {
		
		String[][] data = {{"1"}};

		Table table = asTable(data,"c1");
		
		
		
		Table2CsvDirectives defaultDirectives = new Table2CsvDirectives();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		serialiser.serialise(table,out,defaultDirectives);
		
		
		
		//read and assert round trip
		Csv2TableDirectives parseDirectives = new Csv2TableDirectives();
		parseDirectives.options().setColumns(new Column("c1"));
		Table parsed = parser.parse(new ByteArrayInputStream(out.toByteArray()), parseDirectives);

		System.out.println(table);
		System.out.println(parsed);

		assertEquals(parsed,table);
	}
	
	@Test
	public void serialiseWithHeader() {
		
		String[][] data = {{"1"}};
		Table table = asTable(data,"c1");
		
		Table2CsvDirectives directives = new Table2CsvDirectives();
		directives.options().hasHeader(true);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		serialiser.serialise(table,out,directives);
		
		//read and assert round trip
		Csv2TableDirectives parseDirectives = new Csv2TableDirectives();
		parseDirectives.options().hasHeader(true);
		
		Table parsed = parser.parse(new ByteArrayInputStream(out.toByteArray()), parseDirectives);

		System.out.println(table);
		System.out.println(serialise(parsed));

		assertEquals(parsed,table);
	}
	
	@Test
	public void serialiseWithCustoms() {
		
		String[][] data = {{"1","2"}};
		Table table = asTable(data,"c1","c2");
		
		Table2CsvDirectives directives = new Table2CsvDirectives();
		directives.options().hasHeader(true);
		directives.options().setDelimiter('*');
		directives.options().setQuote('"');
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		serialiser.serialise(table,out,directives);
		
		//read and assert round trip
		Csv2TableDirectives parseDirectives = new Csv2TableDirectives();
		parseDirectives.options().hasHeader(true);
		parseDirectives.options().setDelimiter('*');
		parseDirectives.options().setQuote('"');
		
		Table parsed = parser.parse(new ByteArrayInputStream(out.toByteArray()), parseDirectives);

		System.out.println(table);
		System.out.println(serialise(parsed));

		assertEquals(parsed,table);
	}
	
}