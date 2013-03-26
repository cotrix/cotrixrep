package org.cotrix;

import static java.util.Arrays.*;
import static junit.framework.Assert.*;
import static org.cotrix.TestUtils.*;
import static org.cotrix.domain.dsl.Codes.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.importservice.tabular.csv.CSVOptions;
import org.cotrix.importservice.tabular.csv.CSVTable;
import org.cotrix.importservice.tabular.mapping.AttributeMapping;
import org.cotrix.importservice.tabular.mapping.CodebagMapper;
import org.cotrix.importservice.tabular.mapping.CodebagMapping;
import org.cotrix.importservice.tabular.mapping.CodelistMapper;
import org.cotrix.importservice.tabular.mapping.CodelistMapping;
import org.cotrix.importservice.tabular.model.Table;
import org.junit.Test;

public class MapTest {

	@Test
	public void mapBasicCodelist() throws Exception {

		String[][] data = { { "C1", "C2" }, { "11", "12" }, { "21", "22" } };

		Table table = tableWith(data);

		// prepare mapping
		CodelistMapping mapping = new CodelistMapping("C1");
		
		CodelistMapper mapper = new CodelistMapper(mapping);

		Codelist list = mapper.map(table);

		Codelist expected = codelist()
				.name("C1")
				.with(code().name("11").build(),
					  code().name("21").build()).build();

		assertEquals(expected, list);

	}
	
	@Test
	public void mapCodelistWithNameAndAttributes() throws Exception {

		String[][] data = { { "C1", "C2" }, { "11", "12" }, { "21", "22" } };

		Table table = tableWith(data);

		// prepare mapping
		CodelistMapping mapping = new CodelistMapping("C1");
		
		QName name = q("list");
		mapping.setName(name);
		
		Attribute attribute = attr().name("a").value("v").build();
		mapping.setAttributes(asList(attribute));
		
		CodelistMapper mapper = new CodelistMapper(mapping);

		Codelist list = mapper.map(table);

		Codelist expected = codelist()
				.name(name)
				.with(code().name("11").build(),
					  code().name("21").build())
				.attributes(attribute)
				.build();

		assertEquals(expected, list);

	}

	@Test
	public void mapCodelistWithAttributedCodes() throws Exception {

		String[][] data = { { "C1", "C2", "C3" }, { "11", "12", "13" }, { "21", "22", "23" } };

		Table table = tableWith(data);

		// prepare mapping
		CodelistMapping mapping = new CodelistMapping("C1");
		
		AttributeMapping am1 = new AttributeMapping("C2");
		String attrLang = "en";
		am1.setLanguage(attrLang);
		
		AttributeMapping am2 = new AttributeMapping("C3");
		QName attrName = q("myattribute");
		QName attrType = q("mytype");
		am2.setName(attrName);
		am2.setType(attrType);
		
		mapping.setAttributeMappings(asList(am1,am2));

		CodelistMapper mapper = new CodelistMapper(mapping);

		Codelist list = mapper.map(table);

		Codelist expected = codelist()
				.name("C1")
				.with(code().name("11").attributes(
								attr().name("C2").value("12").in(attrLang).build(),
								attr().name(attrName).value("13").ofType(attrType).build()
							).build(),
					  code().name("21").attributes(
							  attr().name("C2").value("22").in(attrLang).build(),
							  attr().name(attrName).value("23").ofType(attrType).build()
					   )
				      .build())
		.build();

		assertEquals(expected, list);

	}
	
	
	@Test
	public void mapCodebag() throws Exception {

		String[][] data = { { "C1", "C2" }, { "11", "12" }, { "21", "22" } };

		Table table = tableWith(data);

		// prepare mapping
		CodelistMapping mapping1 = new CodelistMapping("C1");
		CodelistMapping mapping2 = new CodelistMapping("C2");
		
		CodebagMapping mapping = new CodebagMapping(q("name"),asList(mapping1,mapping2));

		CodebagMapper mapper = new CodebagMapper(mapping);
		
		Codebag bag = mapper.map(table);

		Codebag expected = codebag()
				.name("name")
				.with(codelist().name("C1").with(
						code().name("11").build(),
						code().name("21").build()
						)
					  .build(),
					  codelist().name("C2").with(
							code().name("12").build(),
							code().name("22").build()
						)
					.build())
				.build();

		assertEquals(expected, bag);

	}

	// helper
	private Table tableWith(String[][] data) {
		return new CSVTable(asCSVStream(data), new CSVOptions());
	}
}
