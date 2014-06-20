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

	private List<QName> targets = new ArrayList<>(defaultTargets); 

	public List<QName> targetAttributes() {
		return targets;
	}
}
