package org.cotrix.io.sdmx;

import org.cotrix.io.parse.ParseDirectives;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

/**
 * {@link ParseDirectives} for SDMX-ML streams.
 * 
 * @author Fabio Simeoni
 *
 */
public final class SdmxParseDirectives implements ParseDirectives<CodelistBean> {

	public static final SdmxParseDirectives DEFAULT  = new SdmxParseDirectives();
	
	private SdmxParseDirectives(){}
	
	@Override
	public String toString() {
		return "no directives (default)";
	}
}
