/**
 * 
 */
package org.cotrix.web.common.shared.codelist.link;

import org.cotrix.web.common.shared.codelist.link.UILinkType.UIValueType;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeNameType implements UIValueType, IsSerializable {
	
	public CodeNameType() {}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return CodeNameType.class.hashCode();
	}

	public boolean equals(Object type) {
		return type instanceof CodeNameType;
	}
}
