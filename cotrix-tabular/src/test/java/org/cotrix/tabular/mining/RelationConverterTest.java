package org.cotrix.tabular.mining;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.core.CotrixException;
import org.cotrix.domain.code.Code;
import org.cotrix.domain.coderelation.Relation1toN;
import org.cotrix.domain.coderelation.Relations1to1;
import org.cotrix.domain.coderelation.Relations1toN;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RelationConverterTest extends RelationConverter {

	RelationConverter relationConverter = new RelationConverter();

	@Test
	public void testConvert1() {
		Relations1toN relations1toN = new Relations1toN();
		List<Relation1toN> relation1toNList = new ArrayList<Relation1toN>();
		relations1toN.setRelation1toNList(relation1toNList);

		Relation1toN relation1toN = new Relation1toN();
		relation1toNList.add(relation1toN);

		List<Code> toCollection = new ArrayList<Code>();
		relation1toN.setToCollection(toCollection);
		Code fromCode = new Code("f");
		Code toCode = new Code("g");
		toCollection.add(toCode);
		relation1toN.setFromCode(fromCode);
		Relations1to1 r = relationConverter.convert(relations1toN);
		assertEquals(fromCode, r.getRelation1to1List().get(0).getSourceCode());
		assertEquals(toCode, r.getRelation1to1List().get(0).getTargetCode());
	}

	@Test
	public void testConvert2() {
		Relations1toN relations1toN = new Relations1toN();
		try {
			relationConverter.convert(relations1toN);
			fail();
		} catch (CotrixException e) {
		}

		List<Relation1toN> relation1toNList = new ArrayList<Relation1toN>();
		relations1toN.setRelation1toNList(relation1toNList);

	}

}
