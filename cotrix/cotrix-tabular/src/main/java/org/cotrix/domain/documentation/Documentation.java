package org.cotrix.domain.documentation;

import java.util.List;

/**
 * Documentation is a set of descriptions in various languages.
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class Documentation {

	protected List<Description> descriptions;

	public List<Description> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<Description> descriptions) {
		this.descriptions = descriptions;
	}

}
