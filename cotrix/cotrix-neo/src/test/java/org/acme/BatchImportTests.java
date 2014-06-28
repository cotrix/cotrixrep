package org.acme;

import static java.lang.System.*;
import static org.cotrix.io.tabular.map.ColumnDirectives.*;

import java.io.InputStream;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.common.cdi.ApplicationEvents.EndRequest;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.MapService;
import org.cotrix.io.MapService.MapDirectives;
import org.cotrix.io.ParseService;
import org.cotrix.io.tabular.csv.parse.Csv2TableDirectives;
import org.cotrix.io.tabular.map.Table2CodelistDirectives;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.test.ApplicationTest;
import org.junit.After;
import org.junit.Test;
import org.virtualrepository.tabular.Table;

public class BatchImportTests extends ApplicationTest {

	@Inject
	MapService service;

	@Inject
	ParseService parser;

	@Inject
	CodelistRepository codelists;
	
	@Inject
	Event<EndRequest> end;
	
	@Test
	public void batchlarge() {
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("large.txt");

		Csv2TableDirectives parseDirectives = new Csv2TableDirectives();
		
		parseDirectives.options().hasHeader(true);
		parseDirectives.options().setDelimiter('\t');
		//parseDirectives.options().setRows(10);
		
		Table table = parser.parse(stream, parseDirectives);

		Outcome<Codelist> outcome = service.map(table, mapDirectives());
		
		ingest(outcome.result());
		
		codelists.remove(outcome.result().id());
		
	}
	
	
	@Test
	public void batchlarger() {
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream("larger.txt");

		Csv2TableDirectives parseDirectives = new Csv2TableDirectives();
		
		parseDirectives.options().hasHeader(true);
		parseDirectives.options().setRows(10);
		
		Table table = parser.parse(stream, parseDirectives);

		Outcome<Codelist> outcome = service.map(table, mapDirectives());
		
		ingest(outcome.result());
		
		
	}
	
	MapDirectives<Codelist> mapDirectives() {

		Table2CodelistDirectives directives = new Table2CodelistDirectives("3A_CODE");
		
		directives.add(column("ISSCAAP"))
				  .add(column("TAXOCODE"))
				  .add(column("Scientific_name"))
				  .add(column("English_name"))
				  .add(column("French_name"))
				  .add(column("Spanish_name"))
				  .add(column("Author"))
				  .add(column("Family"))
				  .add(column("Order"))
				  .add(column("Stats_data"))
				  ;
		
		return directives;
	}
	
	void ingest(Codelist list) {
		
		long time = System.currentTimeMillis();
		
		codelists.add(list);

		System.out.println("done in "+(currentTimeMillis()-time)+" ms.");
	}
	
	@After
	public void after(){
		
		end.fire(EndRequest.INSTANCE);
	}
	
	
}
