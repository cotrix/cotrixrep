package org.cotrix.io.comet.map;

import static org.cotrix.domain.utils.Constants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.io.MapService.MapDirectives;
import org.fao.fi.comet.mapping.model.MappingData;

public class Codelist2CometDirectives implements MapDirectives<MappingData> {

	private static List<QName> defaultTargets = Arrays.asList(PREVIOUS_VERSION_NAME,SUPERSIDES);
	
	public static Codelist2CometDirectives DEFAULT = new Codelist2CometDirectives();

	private List<QName> targets = new ArrayList<>(defaultTargets); 

	private Codelist2CometDirectives() {}
	
	public Codelist2CometDirectives(List<QName> targets) {
		this.targets = targets;
	}

	public List<QName> targetAttributes() {
		return targets;
	}
}
