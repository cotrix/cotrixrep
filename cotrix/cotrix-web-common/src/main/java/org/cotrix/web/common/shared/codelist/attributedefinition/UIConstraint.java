/**
 * 
 */
package org.cotrix.web.common.shared.codelist.attributedefinition;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIConstraint implements IsSerializable {
	
	private String name;
	private List<String> parameters;
	
	public UIConstraint(){
		parameters = new ArrayList<String>();
	}
	
	public UIConstraint(String name, List<String> parameters) {
		this.name = name;
		this.parameters = parameters;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UIConstraint [name=");
		builder.append(name);
		builder.append(", parameters=");
		builder.append(parameters);
		builder.append("]");
		return builder.toString();
	}
}
