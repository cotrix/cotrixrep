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
public class DefinitionsMappings implements IsSerializable {
	
	private List<DefinitionMapping> codelistAttributesMappings;
	
	private List<DefinitionMapping> codesMappings;

	
	public DefinitionsMappings() {
		codesMappings = new ArrayList<DefinitionMapping>();
		codelistAttributesMappings = new ArrayList<DefinitionMapping>();
	}
	
	public DefinitionsMappings(List<DefinitionMapping> codessMappings,
			List<DefinitionMapping> codelistAttributesMapping) {
		this.codesMappings = codessMappings;
		this.codelistAttributesMappings = codelistAttributesMapping;
	}

	public List<DefinitionMapping> getCodesAttributesMapping() {
		return codesMappings;
	}

	public void setCodesAttributesMapping(
			List<DefinitionMapping> codesAttributesMapping) {
		this.codesMappings = codesAttributesMapping;
	}

	public List<DefinitionMapping> getCodelistAttributesMapping() {
		return codelistAttributesMappings;
	}

	public void setCodelistAttributesMapping(
			List<DefinitionMapping> codelistAttributesMapping) {
		this.codelistAttributesMappings = codelistAttributesMapping;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DefinitionsMappings [codesMappings=");
		builder.append(codesMappings);
		builder.append(", codelistAttributesMapping=");
		builder.append(codelistAttributesMappings);
		builder.append("]");
		return builder.toString();
	}
}
