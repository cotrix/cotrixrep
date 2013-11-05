package org.cotrix;

import static org.cotrix.io.tabular.map.ColumnDirectives.*;

import java.io.InputStream;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.ParseService;
import org.cotrix.io.sdmx.map.Sdmx2CodelistDirectives;
import org.cotrix.io.sdmx.parse.Stream2SdmxDirectives;
import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.cotrix.io.tabular.map.Table2CodelistDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class UploadIntegrationTests {

	//we test full upload scenario for sample codelists
	
	@Inject
	ParseService parser;
	
	@Inject
	MapService mapper;
	
	@Test
	public void uploadSdmxSample() {
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("sampleasfissdmx.xml");
		
		CodelistBean bean = parser.parse(stream, Stream2SdmxDirectives.DEFAULT);
		
		Outcome<Codelist> outcome = mapper.map(bean, Sdmx2CodelistDirectives.DEFAULT);
		
		System.out.println(outcome.result());
	}
	
	@Test
	public void uploadAsfisSample() throws Exception {
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("sampleasfiscsv.txt");
		
		Csv2TableDirectives pDirectives = new Csv2TableDirectives();
		pDirectives.options().hasHeader(true);
		pDirectives.options().setDelimiter('\t');
		
		Table table = parser.parse(stream, pDirectives);
		
		Table2CodelistDirectives mDirectives = new Table2CodelistDirectives(new Column("3A_CODE"));
		
		mDirectives.add(column("TAXOCODE"))
				  .add(column("ISSCAAP"))
				  .add(column("Scientific_name"))
				  .add(column("English_name").language("en"))
				  .add(column("French_name").language("fr"))
				  .add(column("Spanish_name").language("es"))
				  .add(column("Author"))
				  .add(column("Family"))
				  .add(column("Order"));
		
		Outcome<Codelist> outcome = mapper.map(table, mDirectives);
		
		System.out.println(outcome.result());
		
	}
	
	
}