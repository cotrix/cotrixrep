package org.cotrix.integration;

import static org.cotrix.domain.dsl.Codes.*;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives;
import org.cotrix.io.sdmx.serialise.Sdmx2XmlDirectives;
import org.cotrix.io.tabular.csv.serialise.Table2CsvDirectives;
import org.cotrix.io.tabular.map.Codelist2TableDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Table;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class ExportTest {

	//won't test mapping or serialisation, just their integration

	@Inject
	MapService mapper;

	@Inject
	SerialisationService serialiser;
	
	@Test
	public void codelist2csv() {
		
		Code code = code().name("c").attributes(attr().name("a").value("aval").build()).build();
		
		Codelist list = codelist().name("list").with(code).build();

		Codelist2TableDirectives directives = new Codelist2TableDirectives();
		
		directives.add(attr().name("a").build());
		
		Outcome<Table> outcome = mapper.map(list,directives);
		
		serialiser.serialise(outcome.result(),System.out,new Table2CsvDirectives());
	
	}

	@Test
	public void codelist2sdmx() {
		
		Code code = code().name("c").attributes(attr().name("a").value("aval").build()).build();
		
		Codelist list = codelist().name("list").with(code).build();

		Codelist2SdmxDirectives directives = new Codelist2SdmxDirectives();
		
		Outcome<CodelistBean> outcome = mapper.map(list,directives);
		
		serialiser.serialise(outcome.result(),System.out,Sdmx2XmlDirectives.DEFAULT);
	
	}

}