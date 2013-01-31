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
 * Parsing tabular data into a codebag. It needs to be investigated whether it makes sense to use also mining. Probably
 * it only makes sense to use parsing.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class TabularService {

	RelationMiner relationMiner = new RelationMiner();
	RelationBuilder relationBuilder = new RelationBuilder();
	Tab2Graph tab2Graph = new Tab2Graph();
	CodeMiner codeMiner = new CodeMiner();

	/**
	 * 
	 * Parse the tabular data into a CodeBag, using Tabular and the TabularMeta data.
	 * 
	 * 
	 * @TODO implement Tab2Graph
	 * 
	 * 
	 * 
	 * @param tabular
	 * @param tabularMeta
	 * @return
	 */
	public CodeBag parse(Tabular tabular, TabularMeta tabularMeta) {
		CodelistContainer c = codeMiner.parseCodes(tabular);
		ConceptRelations conceptRelations = tab2Graph.convert(tabularMeta);
		RelationContainer r = relationBuilder.build(tabular, conceptRelations);
		CodeBag codeJar = new CodeBag();
		codeJar.setCodelistContainer(c);
		codeJar.setRelationContainer(r);
		return codeJar;
	}

	/**
	 * Parse and mine any Tabular without any additional meta data.
	 * 
	 * 
	 * Status: Done for a great deal but some parts maybe missing. This logic
	 * 
	 * @TODO Investigate which parts are missing
	 * @TODO test this one with the ASFIS species list
	 * 
	 * @param tabular
	 * @return a jar with code lists and code relations
	 */
	public CodeBag mine(Tabular tabular) {
		CodelistContainer c = codeMiner.parseCodes(tabular);
		RelationContainer r = relationMiner.mine(tabular, c);
		CodeBag codeJar = new CodeBag();
		codeJar.setCodelistContainer(c);
		codeJar.setRelationContainer(r);
		return codeJar;
	}
}
