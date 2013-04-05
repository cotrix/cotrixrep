package org.cotrix;

import static java.util.Arrays.*;
import static org.cotrix.TestUtils.*;
import static org.junit.Assert.*;

import java.util.List;

import org.cotrix.importservice.tabular.csv.CSVOptions;
import org.cotrix.importservice.tabular.csv.CSVTable;
import org.cotrix.importservice.tabular.model.Row;
import org.cotrix.importservice.tabular.model.Table;
import org.junit.Test;

public class CSVTableTest {

	
	@Test
	public void tableFromCSVWithoutHeaders() throws Exception {
		
		
		List<String> columns = asList("col1","col2");
		
		String[][] data ={{"11","12"},{"21","22"}};
		
		CSVOptions description = new CSVOptions();
		
		description.setColumns(columns,false);
		
		Table table = new CSVTable(asCSVStream(data),description);
		
		assertTrue(equals(table,data));
	}
	
	@Test
	public void tableFromCSVWithHeaders() throws Exception {
		
		String[][] parsed ={{"11","12"},{"21","22"}};
		
		String[][] data ={{"col1","col2"},{"11","12"},{"21","22"}};
		
		Table table = new CSVTable(asCSVStream(data));
		
		assertTrue(equals(table,parsed));
		
	}
	
	@Test
	public void tableFromCSVOverridingHeaders() throws Exception {
		
		
		List<String> columns = asList("col1","col2");
		
		String[][] data ={{"h1","h2"},{"11","12"},{"21","22"}};
		
		
		CSVOptions description = new CSVOptions();
		description.setColumns(columns,true);
		
		Table table = new CSVTable(asCSVStream(data),description);
		
		String[][] expected ={{"11","12"},{"21","22"}};
		
		assertTrue(equals(table,expected));
	}
	
	//helper
	private boolean equals(Table table,String[][] data) {
		
		int i = 0;
		for (Row row : table) {
			int j=0;
			System.out.println(row);
			for (String column : table.columns()) {
				if (!row.get(column).equals(data[i][j]))
						return false;
				else
					j++;
			}
			i++;
		}
		return true;
	}
}
