package org.cotrix;

import static org.cotrix.io.tabular.ColumnDirectives.*;
import static org.junit.Assert.*;

import java.io.InputStream;

import javax.inject.Inject;

import org.cotrix.domain.Codelist;
import org.cotrix.io.ImportService;
import org.cotrix.io.ingest.Outcome;
import org.cotrix.io.sdmx.SdmxImportDirectives;
import org.cotrix.io.tabular.csv.CsvImportDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class UploadIntegrationTests {

	@Inject
	ImportService service;
	
	@Test
	public void servicesAreInjected() throws Exception {
			
		assertNotNull(service);
		
	}
	
	@Test
	public void uploadSdmxCodelist() {
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("samplesdmx.xml");
		
		Outcome<Codelist> outcome = service.importCodelist(stream, SdmxImportDirectives.DEFAULT); 
		
		System.out.println(outcome);
	}
	
	@Test
	public void uploadCsvCodelist() throws Exception {
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("ASFIS2012.txt");
		
		
		CsvImportDirectives directives = new CsvImportDirectives("3A_CODE");
		
		directives.format().hasHeader(true);
		directives.format().setDelimiter('\t');
		directives.format().setRows(10);
		
		directives.add(column("TAXOCODE"))
				  .add(column("ISSCAAP"))
				  .add(column("Scientific_name"))
				  .add(column("English_name").language("en"))
				  .add(column("French_name").language("fr"))
				  .add(column("Spanish_name").language("es"))
				  .add(column("Author"))
				  .add(column("Family"))
				  .add(column("Order"));
		
		Outcome<Codelist> outcome = service.importCodelist(stream, directives);
		
		System.out.println(outcome);
		
	}
	
	
}