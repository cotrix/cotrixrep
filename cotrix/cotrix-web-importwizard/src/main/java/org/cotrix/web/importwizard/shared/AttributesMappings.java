/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributesMappings implements IsSerializable {
	
	protected List<AttributeMapping> mappings;
	protected MappingMode mappingMode;
	
	public AttributesMappings(){}
	
	/**
	 * @param mappings
	 * @param mappingMode
	 */
	public AttributesMappings(List<AttributeMapping> mappings,
			MappingMode mappingMode) {
		this.mappings = mappings;
		this.mappingMode = mappingMode;
	}

	/**
	 * @return the mappings
	 */
	public List<AttributeMapping> getMappings() {
		return mappings;
	}

	/**
	 * @return the mappingMode
	 */
	public MappingMode getMappingMode() {
		return mappingMode;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttributesMappings [mappings=");
		builder.append(mappings);
		builder.append(", mappingMode=");
		builder.append(mappingMode);
		builder.append("]");
		return builder.toString();
	}

}
