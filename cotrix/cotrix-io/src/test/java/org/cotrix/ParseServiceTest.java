package org.cotrix;

import static org.junit.Assert.*;

import java.io.InputStream;

import javax.inject.Inject;

import org.cotrix.io.ParseService;
import org.cotrix.io.sdmx.parse.Stream2SdmxDirectives;
import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Table;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class ParseServiceTest {

	@Inject
	ParseService service;
	
	@Test
	public void servicesAreInjected() throws Exception {
			
		assertNotNull(service);
		
	}
	
	@Test
	public void parseCsvStream() {
		
		//we only need to test dispatching in one case, as parsing capabilities are external to our code and tested elsewhere
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("sampleasfiscsv.txt");
		
		Csv2TableDirectives directives = new Csv2TableDirectives();
		
		directives.options().setDelimiter('\t');
		directives.options().hasHeader(true);
		
		Table table = service.parse(stream, directives);
		
		assertNotNull(table);
	}
	
	@Test
	public void parseHeaderlessCsvStream() {
		
		//we only need to test dispatching in one case, as parsing capabilities are external to our code and tested elsewhere
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("samplenoheader.txt");
		
		Csv2TableDirectives directives = new Csv2TableDirectives();
		
		Table table = service.parse(stream, directives);
		
		assertNotNull(table);
		
		System.out.println(table.columns());
	}
	
	
	@Test
	public void parseSdmxStream() {
		
		//we only need to test dispatching in one case, as parsing capabilities are external to our code and tested elsewhere
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("sampleasfissdmx.xml");
		
		CodelistBean bean = service.parse(stream, Stream2SdmxDirectives.DEFAULT);
		
		assertNotNull(bean);
		
	}

}