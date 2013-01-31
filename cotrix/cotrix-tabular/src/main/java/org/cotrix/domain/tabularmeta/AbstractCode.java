package org.cotrix.domain.tabularmeta;

import java.util.List;

/**
 * A code can have descriptions. A code can have parentcodes.
 * 
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */

public abstract class AbstractCode extends Header {

	/**
	 * A code can have a list of description(s) in every language(s).
	 * 
	 */
	private List<Description> descriptions;

	/**
	 * A code can have one or more parents.
	 * 
	 */
	private List<ParentCode> parentCodeList;

	public List<ParentCode> getParentCodeList() {
		return parentCodeList;
	}

	public void setParentCodeList(List<ParentCode> parentCodeList) {
		this.parentCodeList = parentCodeList;
	}

	public List<Description> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<Description> descriptions) {
		this.descriptions = descriptions;
	}

}
