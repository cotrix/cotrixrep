package org.cotrix.web.importwizard.client.data;

import java.util.ArrayList;
import java.util.List;

public class MockupTabularData {
	private List<String[]> table;
	public List<String[]> getTable() {
		return table;
	}
	public void setTable(List<String[]> table) {
		this.table = table;
	}
	public MockupTabularData() {
		table = new ArrayList<String[]>();
		String[] header = new String[]{"ISSCAAP",
				"TAXOCODE",
				"3A_CODE",
				"Scientific Name",
				"English Name",
				"French Name",
				"Spanish Name",
				"Family",
				"Author"};
		String[] row = new String[]{"25",
				"10293919293",
				"LAU",
				"Petromyzon",
				"Sea lamprey",
				"Lamprea marine",
				"Lamprea marina",
				"Linnaeus 1748",
				"Petromyzontidae"};
		
		table.add(header);
		table.add(row);
		table.add(row);
		table.add(row);
		table.add(row);
		table.add(row);
		table.add(row);
		table.add(row);
		table.add(row);
		table.add(row);
		table.add(row);
		table.add(row);
		table.add(row);
	}
	
}
