package org.cotrix.web.importwizard.server.upload;

import java.io.Serializable;
import java.util.List;

import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MappingGuesser extends Serializable {

	public List<AttributeMapping> guessMappings(Table table);
	
	public List<AttributeMapping> getSdmxDefaultMappings();

}