package org.cotrix.domain.tabularmeta;

/**
 * A codeBag can have codes which have a 1 to 1 relation with other codes. In this case, 1 code is called the code, the
 * others are called siblings.
 * 
 * For instance the ASFIS species list has the 3alphacode and the TAXA code. The codes to have a 1 to 1 relationship.
 * 
 * Parent codes can have also 1 to 1 relationships. These are not yet considered.
 * 
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class SiblingCode extends Code {

	private Code code;

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

}
