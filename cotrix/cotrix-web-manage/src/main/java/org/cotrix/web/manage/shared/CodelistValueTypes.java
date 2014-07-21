/**
 * 
 */
package org.cotrix.web.manage.shared;

import java.util.List;

import org.cotrix.web.common.shared.codelist.linkdefinition.AttributeValue;
import org.cotrix.web.common.shared.codelist.linkdefinition.LinkValue;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistValueTypes implements IsSerializable {
	
	private List<AttributeValue> attributeTypes;
	private List<LinkValue> linkTypes;
	
	public CodelistValueTypes(){}
	
	/**
	 * @param attributeTypes
	 * @param linkTypes
	 */
	public CodelistValueTypes(List<AttributeValue> attributeTypes,
			List<LinkValue> linkTypes) {
		this.attributeTypes = attributeTypes;
		this.linkTypes = linkTypes;
	}

	/**
	 * @return the attributeTypes
	 */
	public List<AttributeValue> getAttributeTypes() {
		return attributeTypes;
	}

	/**
	 * @return the linkTypes
	 */
	public List<LinkValue> getLinkTypes() {
		return linkTypes;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodelistValueTypes [attributeTypes=");
		builder.append(attributeTypes);
		builder.append(", linkTypes=");
		builder.append(linkTypes);
		builder.append("]");
		return builder.toString();
	}
}
