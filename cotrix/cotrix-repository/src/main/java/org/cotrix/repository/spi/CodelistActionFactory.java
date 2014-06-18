package org.cotrix.repository.spi;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.UpdateAction;

/**
 * Factory of update actions over codelists.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface CodelistActionFactory {

	UpdateAction<Codelist> deleteDefinition(String definitionId);

}
