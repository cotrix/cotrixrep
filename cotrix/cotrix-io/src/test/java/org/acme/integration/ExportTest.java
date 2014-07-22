package org.acme.integration;

import static org.cotrix.domain.dsl.Codes.*;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.SerialisationService;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives;
import org.cotrix.io.sdmx.serialise.Sdmx2XmlDirectives;
import org.cotrix.io.tabular.csv.serialise.Table2CsvDirectives;
import org.cotrix.io.tabular.map.Codelist2TableDirectives;
import org.cotrix.test.ApplicationTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sdmx.SdmxServiceFactory;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Table;

public class ExportTest extends ApplicationTest {

	//won't test mapping or serialisation, just their integration

	@Inject
	MapService mapper;

	@Inject
	SerialisationService serialiser;
	
	@BeforeClass
	public static void init() {
		
		SdmxServiceFactory.init();
	}
	
	@Test
	public void codelist2csv() {

		Attribute a = attribute().name("a").value("aval").build();

		Code code = code().name("c").attributes(a).build();
		
		Codelist list = codelist().name("list").with(code).build();

		Codelist2TableDirectives directives = new Codelist2TableDirectives();
		
		directives.add(a);
		
		Outcome<Table> outcome = mapper.map(list,directives);
		
		serialiser.serialise(outcome.result(),System.out,new Table2CsvDirectives());
	
	}

	@Test
	public void codelist2sdmx() {
		
		Code code = code().name("c").attributes(attribute().name("a").value("aval").build()).build();
		
		Codelist list = codelist().name("list").with(code).build();

		Codelist2SdmxDirectives directives = new Codelist2SdmxDirectives();
		
		Outcome<CodelistBean> outcome = mapper.map(list,directives);
		
		serialiser.serialise(outcome.result(),System.out,Sdmx2XmlDirectives.DEFAULT);
	
	}

}