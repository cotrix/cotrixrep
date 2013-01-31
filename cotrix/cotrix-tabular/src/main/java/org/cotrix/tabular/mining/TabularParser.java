package org.cotrix.tabular.mining;

import org.cotrix.domain.bag.CodeBag;
import org.cotrix.domain.code.CodelistContainer;
import org.cotrix.domain.coderelation.RelationContainer;
import org.cotrix.domain.tabular.Tabular;
import org.cotrix.domain.tabularmeta.TabularMeta;

/**
 * 
 * @author Erik van Ingen
 * 
 */
public class TabularParser {

	RelationMiner relationMiner = new RelationMiner();
	CodeMiner codeMiner = new CodeMiner();

	/**
	 * Parse and mine any Tabular without any additional metadata.
	 * 
	 * 
	 * 
	 * @param tabular
	 * @return a jar with code lists and code relations
	 */
	public CodeBag parse(Tabular tabular) {
		CodelistContainer c = codeMiner.parseCodes(tabular);
		RelationContainer r = relationMiner.mine(tabular, c);
		CodeBag codeJar = new CodeBag();
		codeJar.setCodelistContainer(c);
		codeJar.setRelationContainer(r);
		return codeJar;
	}

	/**
	 * 
	 * @param tabular
	 * @param tabularMeta
	 * @return
	 */
	public CodeBag parse(Tabular tabular, TabularMeta tabularMeta) {
		CodeBag codeJar = new CodeBag();
		return codeJar;
	}

}
