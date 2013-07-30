package org.cotrix.web.importwizard.server.upload;

import java.util.List;

import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.virtualrepository.tabular.Table;

public interface MappingGuesser {

	public abstract List<AttributeMapping> guessMappings(Table table);

}