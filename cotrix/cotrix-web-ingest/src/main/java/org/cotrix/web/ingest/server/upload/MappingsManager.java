/**
 * 
 */
package org.cotrix.web.ingest.server.upload;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.cotrix.web.ingest.shared.AttributeMapping;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SessionScoped
public class MappingsManager implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2875724067244508182L;

	@Inject
	protected MappingGuesser mappingsGuesser;
	
	protected List<AttributeMapping> mappings;
	
	public void updateMappings(Table table) {
		 mappings = mappingsGuesser.guessMappings(table);
	}
	
	public void setDefaultSdmxMappings() {
		mappings = mappingsGuesser.getSdmxDefaultMappings();
	}

	/**
	 * @return the mappings
	 */
	public List<AttributeMapping> getMappings() {
		return mappings;
	}

}
