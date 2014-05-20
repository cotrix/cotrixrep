/**
 * 
 */
package org.cotrix.web.publish.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributesMappings implements IsSerializable {
	
	private List<AttributeMapping> codesAttributesMapping;
	private List<AttributeMapping> codelistAttributesMapping;
	
	public AttributesMappings() {
		codesAttributesMapping = new ArrayList<AttributeMapping>();
		codelistAttributesMapping = new ArrayList<AttributeMapping>();
	}
	
	public AttributesMappings(List<AttributeMapping> codesAttributesMapping,
			List<AttributeMapping> codelistAttributesMapping) {
		this.codesAttributesMapping = codesAttributesMapping;
		this.codelistAttributesMapping = codelistAttributesMapping;
	}

	/**
	 * @return the codesAttributesMapping
	 */
	public List<AttributeMapping> getCodesAttributesMapping() {
		return codesAttributesMapping;
	}
	/**
	 * @param codesAttributesMapping the codesAttributesMapping to set
	 */
	public void setCodesAttributesMapping(
			List<AttributeMapping> codesAttributesMapping) {
		this.codesAttributesMapping = codesAttributesMapping;
	}
	/**
	 * @return the codelistAttributesMapping
	 */
	public List<AttributeMapping> getCodelistAttributesMapping() {
		return codelistAttributesMapping;
	}
	/**
	 * @param codelistAttributesMapping the codelistAttributesMapping to set
	 */
	public void setCodelistAttributesMapping(
			List<AttributeMapping> codelistAttributesMapping) {
		this.codelistAttributesMapping = codelistAttributesMapping;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttributeMappings [codesAttributesMapping=");
		builder.append(codesAttributesMapping);
		builder.append(", codelistAttributesMapping=");
		builder.append(codelistAttributesMapping);
		builder.append("]");
		return builder.toString();
	}
}
