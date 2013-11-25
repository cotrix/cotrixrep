package org.cotrix.io.tabular.map;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.io.impl.MapTask;
import org.virtualrepository.tabular.Table;

// since model it's streamed, combines parser and model
public class Table2CodelistTask implements MapTask<Table, Codelist, Table2CodelistDirectives> {

	@Override
	public Class<Table2CodelistDirectives> directedBy() {
		return Table2CodelistDirectives.class;
	}

	@Override
	public Codelist map(Table table, Table2CodelistDirectives directives) throws Exception {

		Table2Codelist transform = new Table2Codelist(directives);

		return transform.apply(table);
		
	}
	
	@Override
	public String toString() {
		return "table-mapper";
	}
}
