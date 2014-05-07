package org.cotrix.io.comet.map;

import org.cotrix.io.MapService.MapDirectives;
import org.fao.fi.comet.mapping.model.MappingData;

public class Codelist2CometDirectives implements MapDirectives<MappingData> {

	public static Codelist2CometDirectives DEFAULT = new Codelist2CometDirectives();

	private Codelist2CometDirectives() {}	
}
