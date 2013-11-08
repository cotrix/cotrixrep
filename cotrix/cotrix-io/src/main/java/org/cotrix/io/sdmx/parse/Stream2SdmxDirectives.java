package org.cotrix.io.sdmx.parse;

import org.cotrix.io.ParseService.ParseDirectives;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;


/**
 * Directives to parse SDMX-ML streams.
 * 
 * @author Fabio Simeoni
 *
 */
public final class Stream2SdmxDirectives implements ParseDirectives<CodelistBean>  {

	/**
	 * Singleton instance.
	 */
	public static final Stream2SdmxDirectives DEFAULT  = new Stream2SdmxDirectives();
	
	private Stream2SdmxDirectives(){}
	
	@Override
	public String toString() {
		return "no directives (default)";
	}
}
