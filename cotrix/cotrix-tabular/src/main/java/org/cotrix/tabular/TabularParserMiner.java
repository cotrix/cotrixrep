package org.cotrix.tabular;

import org.cotrix.domain.bag.CodeBag;
import org.cotrix.domain.code.CodelistContainer;
import org.cotrix.domain.coderelation.RelationContainer;
import org.cotrix.domain.conceptrelation.ConceptRelations;
import org.cotrix.domain.tabular.Tabular;
import org.cotrix.domain.tabularmeta.TabularMeta;
import org.cotrix.tabular.mining.CodeMiner;
import org.cotrix.tabular.mining.RelationMiner;

/**
 * 
 * @author Erik van Ingen
 * 
 */
public class TabularParserMiner {

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

	/**
	 * Parses tabular data, using the relations as metadata
	 * 
	 * 
	 * 
	 * 
	 * @param tabular
	 * @param relations
	 * @return RelationContainer with the relevant relations
	 */
	public RelationContainer parse(Tabular tabular, ConceptRelations relations) {
		RelationContainer r = new RelationContainer();

		return r;
	}

}
