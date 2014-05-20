package org.cotrix.web.ingest.server.upload;

import java.io.Serializable;
import java.util.List;

import org.cotrix.web.ingest.shared.AttributeMapping;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MappingGuesser extends Serializable {

	public List<AttributeMapping> guessMappings(List<String> userLabels);

}