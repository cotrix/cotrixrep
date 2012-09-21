package org.cotrix.domain.coderelation;

import java.util.List;

import org.cotrix.domain.code.Code;

/**
 * Not yet engineered/supported. The questions is whether it is needed from a
 * practical point of view. It can be derived from the 1toN relations.
 * 
 * 
 * @author Erik van Ingen
 * 
 */

public class RelationNto1 extends CodeRelation {

	protected List<Code> sourceCollection;
	protected Code targetCode;

	public List<Code> getSourceCollection() {
		return sourceCollection;
	}

	public void setSourceCollection(List<Code> sourceCollection) {
		this.sourceCollection = sourceCollection;
	}

	public Code getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(Code targetCode) {
		this.targetCode = targetCode;
	}

}
