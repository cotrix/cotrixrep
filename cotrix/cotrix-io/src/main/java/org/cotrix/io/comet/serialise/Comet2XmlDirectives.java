package org.cotrix.io.comet.serialise;

import org.cotrix.io.SerialisationService.SerialisationDirectives;
import org.fao.fi.comet.mapping.model.MappingData;

public class Comet2XmlDirectives implements SerialisationDirectives<MappingData> {

	public static final Comet2XmlDirectives DEFAULT  = new Comet2XmlDirectives();
	
	private Comet2XmlDirectives(){}
	
	@Override
	public String toString() {
		return "no directives (default)";
	}
}
