/**
 * 
 */
package org.cotrix.web.common.shared.codelist.linkdefinition;

import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition.UIValueType;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeNameValue implements UIValueType, IsSerializable {
	
	public CodeNameValue() {}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return CodeNameValue.class.hashCode();
	}

	public boolean equals(Object type) {
		return type instanceof CodeNameValue;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodeNameType []");
		return builder.toString();
	}
}
