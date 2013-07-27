package org.cotrix.io.tabular;

import org.cotrix.domain.Codelist;
import org.cotrix.io.map.MapTask;
import org.virtualrepository.tabular.Table;

// since model it's streamed, combines parser and model
public class TableMapTask implements MapTask<Table, TableMapDirectives> {

	@Override
	public Class<TableMapDirectives> directedBy() {
		return TableMapDirectives.class;
	}

	@Override
	public Codelist map(Table table, TableMapDirectives directives) throws Exception {

		Table2Codelist transform = new Table2Codelist(directives);

		return transform.apply(table);
		
	}
	
	@Override
	public String toString() {
		return "table-mapper";
	}
}
