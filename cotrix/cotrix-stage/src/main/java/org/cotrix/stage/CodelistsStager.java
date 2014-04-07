package org.cotrix.stage;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.stage.data.SomeCodelists.*;
import static org.cotrix.stage.data.SomeUsers.*;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.common.cdi.ApplicationEvents.FirstTime;
import org.cotrix.common.cdi.ApplicationEvents.Ready;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.user.User;
import org.cotrix.io.MapService;
import org.cotrix.io.ParseService;
import org.cotrix.io.sdmx.map.Sdmx2CodelistDirectives;
import org.cotrix.io.sdmx.parse.Stream2SdmxDirectives;
import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.cotrix.io.tabular.map.ColumnDirectives;
import org.cotrix.io.tabular.map.Table2CodelistDirectives;
import org.cotrix.security.SignupService;
import org.cotrix.stage.data.SomeCodelists.Info;
import org.cotrix.stage.data.SyntheticCodelists;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

public class CodelistsStager {

	@Inject
	ParseService parser;

	@Inject
	MapService mapper;

	@Inject
	CodelistIngester ingester;
	
	@Inject
	SignupService service;

	static private final Logger log = LoggerFactory.getLogger(CodelistsStager.class);

	
	public void stage(@Observes @FirstTime Ready event) {
		
		for (User u : users)
			service.signup(u,u.name());

		for (Info info : CSV_CODELISTS)
			stageCSV(info);

		for (Info info : SDMX_CODELISTS)
			stageSDMX(info);

		
		
		Codelist list = SyntheticCodelists.demo();
		ingester.ingest(list);
		
		Codelist linked = SyntheticCodelists.linked(list);
		
		ingester.ingest(linked);
		
//		for(Codelist list : SyntheticCodelists.synthetics)
//			ingester.ingest(list);

	}

	void stageCSV(Info info) {

		log.info("staging {}", info.name);

		InputStream inputStream = CodelistsStager.class.getResourceAsStream(info.resource);

		Csv2TableDirectives parseDirectives = new Csv2TableDirectives();
		parseDirectives.options().hasHeader(true);
		parseDirectives.options().setDelimiter('\t');
		parseDirectives.options().setQuote('"');
		parseDirectives.options().setEncoding(Charset.forName("UTF-8"));

		Table table = parser.parse(inputStream, parseDirectives);

		List<ColumnDirectives> directives = new ArrayList<ColumnDirectives>();
		Column codeColumn = null;
		
		for (Column column : table.columns())
			if (column.name().toString().equals(info.code))
				codeColumn = column;
			else
				directives.add(new ColumnDirectives(column));

		if (codeColumn == null)
			throw new AssertionError("bad fixture: no column with name " + info.code);

		Table2CodelistDirectives mappingDirectives = new Table2CodelistDirectives(codeColumn);
		mappingDirectives.name(info.name);
		mappingDirectives.version(info.version);

		for (ColumnDirectives directive : directives)
			mappingDirectives.add(directive);

		Attribute att1 = attribute().name("filename").value(info.resource).in("English").build();
		Attribute att2 = attribute().name("format").value("CSV").in("English").build();

		mappingDirectives.attributes(att1, att2);

		Outcome<Codelist> outcome = mapper.map(table, mappingDirectives);

		ingester.ingest(outcome.result());
	}

	void stageSDMX(Info info) {

		log.info("staging {}({})", info.name, info.version);

		InputStream inputStream = CodelistsStager.class.getResourceAsStream(info.resource);

		CodelistBean bean = parser.parse(inputStream, Stream2SdmxDirectives.DEFAULT);

		Outcome<Codelist> outcome = mapper.map(bean, Sdmx2CodelistDirectives.DEFAULT);

		ingester.ingest(outcome.result());

	}


}
