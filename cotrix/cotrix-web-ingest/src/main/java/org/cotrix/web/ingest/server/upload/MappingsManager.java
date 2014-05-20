/**
 * 
 */
package org.cotrix.web.ingest.server.upload;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;

import org.cotrix.web.ingest.shared.AttributeMapping;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SessionScoped
public class MappingsManager implements Serializable {
	
	private static final long serialVersionUID = 2875724067244508182L;

	private MappingGuesser mappingsGuesser;
	
	public void setMappingGuesser(MappingGuesser mappingGuesser) {
		this.mappingsGuesser = mappingGuesser;
	}
	
	public List<AttributeMapping> getMappings(List<String> userLabels) {
		return mappingsGuesser.guessMappings(userLabels);
	}
}
