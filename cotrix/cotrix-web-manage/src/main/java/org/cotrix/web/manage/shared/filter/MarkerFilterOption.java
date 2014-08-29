/**
 * 
 */
package org.cotrix.web.manage.shared.filter;

import java.util.Set;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MarkerFilterOption implements FilterOption {
	
	private Set<String> definitionsNames;
	
	public MarkerFilterOption(){}

	public MarkerFilterOption(Set<String> definitionsNames) {
		this.definitionsNames = definitionsNames;
	}

	public Set<String> getDefinitionsNames() {
		return definitionsNames;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MarkerFilterOption [definitionsNames=");
		builder.append(definitionsNames);
		builder.append("]");
		return builder.toString();
	}

}
