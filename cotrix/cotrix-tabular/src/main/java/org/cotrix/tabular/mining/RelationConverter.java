package org.cotrix.tabular.mining;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.core.CotrixException;
import org.cotrix.domain.code.Code;
import org.cotrix.domain.coderelation.Relation1to1;
import org.cotrix.domain.coderelation.Relation1toN;
import org.cotrix.domain.coderelation.Relations1to1;
import org.cotrix.domain.coderelation.Relations1toN;

/**
 * 
 * Conversion of cardinalities of relationships.
 * 
 * 
 * In the process a 1 to N relationship is assumed. This could be in real a 1 to to relationship. In this case the
 * relations need to be converted and that is done in this class.
 * 
 * @author Erik van Ingen
 * 
 */
public class RelationConverter {

	/**
	 * The index of the first element is 0
	 */
	private static final int FIRST = 0;
	private static final int MAX = 1;
	private static final int EMPTY = 0;

	/**
	 * 
	 * Covert the polygamics into monogamics
	 * 
	 * 
	 * @param relations1toN
	 * @return relations1to1
	 */
	public Relations1to1 convert(Relations1toN relations1toN) {
		List<Relation1toN> old = relations1toN.getRelation1toNList();
		if (old == null) {
			throw new CotrixException("There is not relation list defined");
		}
		List<Relation1to1> neww = new ArrayList<Relation1to1>();
		Relations1to1 relations1to1 = new Relations1to1();
		relations1to1.setRelation1to1List(neww);
		relations1to1.setSourceConcept(relations1toN.getSourceConcept());
		relations1toN.setTargetConcept(relations1toN.getTargetConcept());

		for (Relation1toN relation1toN : old) {
			List<Code> cods = relation1toN.getToCollection();
			if (cods.size() > MAX) {
				throw new CotrixException(
						"When converting from 1-n to 1-1, the number of elements in the target collection needs to be 1, was "
								+ cods.size());
			}
			if (cods.size() == EMPTY) {
				throw new CotrixException("There is no target collection, so this is a corrupt relation " + cods.size());
			}
			Relation1to1 relation1to1 = new Relation1to1();
			relation1to1.setSourceCode(relation1toN.getFromCode());
			relation1to1.setTargetCode(cods.get(FIRST));
			neww.add(relation1to1);
		}

		return relations1to1;
	}
}
