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

	UpdateAction<Codelist> deleteAttrdef(String definitionId);
	
	UpdateAction<Codelist> deleteLinkdef(String linkId);

}
