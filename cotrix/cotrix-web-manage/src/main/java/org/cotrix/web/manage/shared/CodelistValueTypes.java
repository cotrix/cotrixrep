/**
 * 
 */
package org.cotrix.web.manage.shared;

import java.util.List;

import org.cotrix.web.common.shared.codelist.link.AttributeType;
import org.cotrix.web.common.shared.codelist.link.LinkType;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistValueTypes implements IsSerializable {
	
	private List<AttributeType> attributeTypes;
	private List<LinkType> linkTypes;
	
	public CodelistValueTypes(){}
	
	/**
	 * @param attributeTypes
	 * @param linkTypes
	 */
	public CodelistValueTypes(List<AttributeType> attributeTypes,
			List<LinkType> linkTypes) {
		this.attributeTypes = attributeTypes;
		this.linkTypes = linkTypes;
	}

	/**
	 * @return the attributeTypes
	 */
	public List<AttributeType> getAttributeTypes() {
		return attributeTypes;
	}

	/**
	 * @return the linkTypes
	 */
	public List<LinkType> getLinkTypes() {
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
