package org.cotrix.io.sdmx.serialise;

import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class Sdmx2StreamDirectives implements SerialisationDirectives<CodelistBean> {

	public static final Sdmx2StreamDirectives DEFAULT  = new Sdmx2StreamDirectives();
	
	private Sdmx2StreamDirectives(){}
	
	@Override
	public String toString() {
		return "no directives (default)";
	}
}
