package org.cotrix.importservice.tabular.csv;

import org.cotrix.domain.Codelist;
import org.cotrix.importservice.tabular.mapping.CodelistMapping;

/**
 * Directives for importing a {@link Codelist} from CSV data.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CSV2Codelist extends CSVDirectives<Codelist,CodelistMapping> {

	/**
	 * Creates an instance with a given mapping and given parsing options.
	 * @param mapping the mapping
	 * @param options the options
	 */
	public CSV2Codelist(CodelistMapping mapping,CSVOptions options) {
		super(mapping,options);
	}
	
	/**
	 * Creates an instance with a given mapping and default parsing options.
	 * @param mapping the mapping
	 */
	public CSV2Codelist(CodelistMapping mapping) {
		super(mapping,new CSVOptions());
	}
	
}
