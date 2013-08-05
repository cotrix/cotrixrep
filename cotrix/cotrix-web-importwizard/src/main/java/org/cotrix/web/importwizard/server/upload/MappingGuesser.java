package org.cotrix.web.importwizard.server.upload;

import org.cotrix.web.importwizard.shared.AttributesMappings;
import org.virtualrepository.tabular.Table;

public interface MappingGuesser {

	public AttributesMappings guessMappings(Table table);
	
	public AttributesMappings getSdmxDefaultMappings();

}