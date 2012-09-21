package org.cotrix.domain.code;

import java.util.List;

/**
 * A codelist is a list of Codes. It expresses all the possible values which can
 * be used for a dimension, attribute or whatsoever.
 * 
 * @author Erik van Ingen
 * 
 */
public class Codelist {

	protected List<Code> codelist;

	public List<Code> getCodelist() {
		return codelist;
	}

	public void setCodelist(List<Code> codelist) {
		this.codelist = codelist;
	}

}
