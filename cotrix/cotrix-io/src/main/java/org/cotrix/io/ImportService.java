package org.cotrix.io;

import java.io.InputStream;

import org.cotrix.domain.Codelist;
import org.cotrix.io.ingest.ImportDirectives;
import org.cotrix.io.ingest.Outcome;
import org.virtualrepository.Asset;

/**
 * Imports codelists from users or external repository services.
 * 
 * @author Fabio Simeoni
 *
 */
public interface ImportService {

	/**
	 * Imports an uploaded codelist using given directives.
	 * @param codelist the codelist
	 * @param directives the directives
	 * @return the outcome of the task
	 * 
	 */
	Outcome<Codelist> importCodelist(InputStream codelist, ImportDirectives directives);
	
	/**
	 * Returns the external codelists available for import, using a default timeout. 
	 * @return the external codelists
	 */
	Iterable<Asset> remoteCodelists();
	
	/**
	 * Discovers remote codelists available for import, using a given timeout (in seconds). 
	 * 
	 * @param the timeout (in seconds)
	 * @return the number of codelists that have been discovered
	 */
	int discoverRemoteCodelists(int timeout);
	
	
	/**
	 * Discovers remote codelists available for import. 
	 * @return the number of codelists that have been discovered
	 */
	int discoverRemoteCodelists();
	
	/**
	 * Imports a remote codelist using given directives.
	 * @param id the identifier of the codelist
	 * @param directives the directives
	 * @return the outcome of the task
	 */
	Outcome<Codelist> importCodelist(String id, ImportDirectives directives);
}
