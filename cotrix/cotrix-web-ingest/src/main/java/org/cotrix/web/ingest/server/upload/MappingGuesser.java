package org.cotrix.web.ingest.server.upload;

import java.io.Serializable;
import java.util.List;

import org.cotrix.web.ingest.shared.AttributeMapping;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MappingGuesser extends Serializable {

	public List<AttributeMapping> guessMappings(Table table);
	
	public List<AttributeMapping> getSdmxDefaultMappings();

}