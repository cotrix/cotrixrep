package org.cotrix;

import static java.util.Arrays.*;
import static junit.framework.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;

import java.io.File;
import java.io.FileInputStream;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.domain.Codelist;
import org.cotrix.importservice.ImportService;
import org.cotrix.importservice.Outcome;
import org.cotrix.importservice.tabular.csv.CSV2Codelist;
import org.cotrix.importservice.tabular.csv.CSVOptions;
import org.cotrix.importservice.tabular.mapping.AttributeMapping;
import org.cotrix.importservice.tabular.mapping.CodelistMapping;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class ASFISImportTest {

	@Inject
	ImportService service;
	
	@Test
	public void importASFIS() throws Exception {

		File file = new File("src/test/resources/ASFIS/2012/original/ASFIS_sp_Feb_2012.txt");
		
		CSVOptions options = new CSVOptions();
		options.setDelimiter('\t');
		options.setRows(10);
		
		CodelistMapping mapping = new CodelistMapping("3A_CODE");
		QName asfisName = q("asfis-2012");
		mapping.setName(asfisName);
		
		AttributeMapping taxo = new AttributeMapping("TAXOCODE");
		AttributeMapping isscaap = new AttributeMapping("ISSCAAP");
		
		AttributeMapping scientific = new AttributeMapping("Scientific_name");
		AttributeMapping english = new AttributeMapping("English_name");
		english.setLanguage("en");
		
		AttributeMapping french = new AttributeMapping("French_name");
		french.setLanguage("fr");
		
		AttributeMapping spanish = new AttributeMapping("Spanish_name");
		spanish.setLanguage("es");
		
		AttributeMapping author = new AttributeMapping("Author");
		AttributeMapping family = new AttributeMapping("Family");
		AttributeMapping order = new AttributeMapping("Order");
		
		mapping.setAttributeMappings(asList(taxo,isscaap,scientific,english,french,spanish,author,family,order));
		
		CSV2Codelist directives = new CSV2Codelist(mapping,options);
		
		Outcome<Codelist> outcome = service.importCodelist(new FileInputStream(file),directives);
		
		System.out.println(outcome.report());
		
		Codelist asfis = outcome.result();
		
		//System.out.println(asfis.codes());
		assertEquals(asfisName, asfis.name());
		
	}
}
