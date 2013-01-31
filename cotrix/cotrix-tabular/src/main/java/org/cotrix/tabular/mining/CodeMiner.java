package org.cotrix.tabular.mining;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.code.Code;
import org.cotrix.domain.code.Codelist;
import org.cotrix.domain.code.CodelistContainer;
import org.cotrix.domain.tabular.Tabular;

public class CodeMiner {

	public CodelistContainer parseCodes(Tabular tabular) {
		CodelistContainer container = new CodelistContainer();
		List<Codelist> list = new ArrayList<Codelist>();
		container.setList(list);

		List<List<String>> rows = tabular.getRows();
		int nrOfColumns = rows.get(0).size();
		int nrOfRows = rows.size();

		// loop through the columns, in order to retrieve the lists of code
		for (int c = 0; c < nrOfColumns; c++) {
			Codelist codelist = new Codelist();
			list.add(codelist);
			List<Code> cl = new ArrayList<Code>();
			codelist.setCodelist(cl);
			// loop through the elements of the list by looping through the
			// rows.
			for (int r = 0; r < nrOfRows; r++) {
				String cell = rows.get(r).get(c);
				Code code = new Code();
				code.setValue(cell);

				if (!cl.contains(code)) {
					// add only if not already exist in the list
					cl.add(code);
				}
			}
		}
		return container;
	}

}
