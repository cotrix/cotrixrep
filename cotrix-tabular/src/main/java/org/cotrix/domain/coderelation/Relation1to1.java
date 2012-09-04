package org.cotrix.domain.coderelation;

import org.cotrix.domain.code.Code;

/**
 * In case of a relation of one code with only one other code.
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class Relation1to1 extends CodeRelation {

	protected Code sourceCode;
	protected Code targetCode;

	public Code getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(Code sourceCode) {
		this.sourceCode = sourceCode;
	}

	public Code getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(Code targetCode) {
		this.targetCode = targetCode;
	}

}
