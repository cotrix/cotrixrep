package org.cotrix.importservice;

import java.io.InputStream;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;

/**
 * Imports data into {@link Codelist}s or {@link Codebag}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface ImportService {

	/**
	 * Imports data into a {@link Codelist}.
	 * @param data the data
	 * @param directives {@link Directives} for the import task
	 * @return the {@link Outcome} of the operation
	 */
	Outcome<Codelist> importCodelist(InputStream data, Directives<Codelist> directives);
	
	/**
	 * Imports data into a {@link Codebag}.
	 * @param data the data
	 * @param directives {@link Directives} for the import task
	 * @return the {@link Outcome} of the operation
	 */
	Outcome<Codebag> importCodebag(InputStream data, Directives<Codebag> directives);
}
