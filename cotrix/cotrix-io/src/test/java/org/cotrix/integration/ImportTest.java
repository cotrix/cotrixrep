package org.cotrix.integration;

import static org.cotrix.TestUtils.*;

import java.io.InputStream;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.ParseService;
import org.cotrix.io.sdmx.map.Sdmx2CodelistDirectives;
import org.cotrix.io.sdmx.parse.Stream2SdmxDirectives;
import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.cotrix.io.tabular.map.Table2CodelistDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Table;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class ImportTest {

	//won't test parsing or mapping, just their integration

	@Inject
	MapService service;

	@Inject
	ParseService parser;
	
	@Test
	public void csv2codelist() {
		
		String[][] data = {{"c1"},{"1"}};

		Csv2TableDirectives parseDirectives = new Csv2TableDirectives();
		
		parseDirectives.options().hasHeader(true);
		
		InputStream stream = asCsv(data,parseDirectives);
		
		Table table = parser.parse(stream, parseDirectives);

		Table2CodelistDirectives directives = new Table2CodelistDirectives("c1");

		Outcome<Codelist> outcome = service.map(table, directives);
		
		System.out.println(outcome.result());
		System.out.println(outcome.report());

	}

	@Test
	public void sdmx2Codelist() throws Exception {

		InputStream stream = getClass().getClassLoader().getResourceAsStream("samplesdmx.xml");

		CodelistBean bean = parser.parse(stream,Stream2SdmxDirectives.DEFAULT);

		Outcome<Codelist> outcome = service.map(bean, new Sdmx2CodelistDirectives().name("test"));

		System.out.println(outcome.result());
		System.out.println(outcome.report());

	}

}