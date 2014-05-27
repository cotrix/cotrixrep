/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attributetype.constraint;

import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetaConstraint {
	
	private String name;
	private List<String> arguments;
	
	public MetaConstraint(String name, List<String> arguments) {
		this.name = name;
		this.arguments = arguments;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getArguments() {
		return arguments;
	}
	
	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetaConstraint [name=");
		builder.append(name);
		builder.append(", arguments=");
		builder.append(arguments);
		builder.append("]");
		return builder.toString();
	}
}
