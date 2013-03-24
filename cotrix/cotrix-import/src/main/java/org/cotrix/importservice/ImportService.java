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
	 * @param directives directives for the import task
	 * @return the imported code list
	 */
	Codelist importCodelist(InputStream data, Directives<Codelist> directives);
	
	/**
	 * Imports data into a {@link Codebag}.
	 * @param data the data
	 * @param directives directives for the import task
	 * @return the imported code bag
	 */
	Codebag importCodebag(InputStream data, Directives<Codebag> directives);
}
