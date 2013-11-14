/**
 * 
 */
package org.cotrix.web.publish.shared;

import java.util.List;

import org.cotrix.web.share.shared.CsvConfiguration;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishDirectives implements IsSerializable {

	protected String codelistId;
	protected PublishMetadata metadata;
	protected List<AttributeMapping> mappings;
	protected MappingMode mappingMode;
	protected CsvConfiguration csvConfiguration;
	
	/**
	 * @return the codelistId
	 */
	public String getCodelistId() {
		return codelistId;
	}
	
	/**
	 * @param codelistId the codelistId to set
	 */
	public void setCodelistId(String codelistId) {
		this.codelistId = codelistId;
	}
	
	/**
	 * @return the metadata
	 */
	public PublishMetadata getMetadata() {
		return metadata;
	}
	
	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(PublishMetadata metadata) {
		this.metadata = metadata;
	}
	
	/**
	 * @return the mappings
	 */
	public List<AttributeMapping> getMappings() {
		return mappings;
	}
	
	/**
	 * @param mappings the mappings to set
	 */
	public void setMappings(List<AttributeMapping> mappings) {
		this.mappings = mappings;
	}
	
	/**
	 * @return the mappingMode
	 */
	public MappingMode getMappingMode() {
		return mappingMode;
	}
	
	/**
	 * @param mappingMode the mappingMode to set
	 */
	public void setMappingMode(MappingMode mappingMode) {
		this.mappingMode = mappingMode;
	}
	
	/**
	 * @return the csvConfiguration
	 */
	public CsvConfiguration getCsvConfiguration() {
		return csvConfiguration;
	}
	
	/**
	 * @param csvConfiguration the csvConfiguration to set
	 */
	public void setCsvConfiguration(CsvConfiguration csvConfiguration) {
		this.csvConfiguration = csvConfiguration;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PublishDirectives [codelistId=");
		builder.append(codelistId);
		builder.append(", metadata=");
		builder.append(metadata);
		builder.append(", mappings=");
		builder.append(mappings);
		builder.append(", mappingMode=");
		builder.append(mappingMode);
		builder.append(", csvConfiguration=");
		builder.append(csvConfiguration);
		builder.append("]");
		return builder.toString();
	}
}
