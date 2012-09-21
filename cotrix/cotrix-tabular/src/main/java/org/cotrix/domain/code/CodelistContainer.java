package org.cotrix.domain.code;

import java.util.List;

/**
 * A container for Codelists
 * 
 * This could be the interpretation of a csv file, containing codes.
 * 
 * 
 * 
 * @author Erik
 * 
 */

public class CodelistContainer {

	protected List<Codelist> list;

	public List<Codelist> getList() {
		return list;
	}

	public void setList(List<Codelist> list) {
		this.list = list;
	}

}
