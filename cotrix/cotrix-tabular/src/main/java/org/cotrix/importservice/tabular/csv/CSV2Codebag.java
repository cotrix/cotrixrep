package org.cotrix.importservice.tabular.csv;

import org.cotrix.domain.Codebag;
import org.cotrix.importservice.tabular.mapping.CodebagMapping;

/**
 * Directives for importing a {@link Codebag} from CSV data.
 * 
 * @author Fabio Simeoni
 *
 */
public class CSV2Codebag extends CSVDirectives<Codebag,CodebagMapping> {

	/**
	 * Creates an instance with a given mapping and given parsing options.
	 * @param mapping the mapping
	 * @param options the options
	 */
	public CSV2Codebag(CodebagMapping mapping,CSVOptions options) {
		super(mapping,options);
	}
	
	/**
	 * Creates an instance with a given mapping and default parsing options.
	 * @param mapping the mapping
	 */
	public CSV2Codebag(CodebagMapping mapping) {
		super(mapping,new CSVOptions());
	}
	
}
