package org.cotrix.io.sdmx.serialise;

import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;

public class Sdmx2XmlDirectives implements SerialisationDirectives<CodelistBean> {

	public static final Sdmx2XmlDirectives DEFAULT  = new Sdmx2XmlDirectives();
	
	private Sdmx2XmlDirectives(){}
	
	@Override
	public String toString() {
		return "no directives (default)";
	}
}
