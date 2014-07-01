package org.cotrix.io.comet.map;

import static java.util.Arrays.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.io.MapService.MapDirectives;
import org.fao.fi.comet.mapping.model.MappingData;

public class Codelist2CometDirectives implements MapDirectives<MappingData> {

	private static List<QName> defaultTargets = asList(PREVIOUS_VERSION_NAME.qname(),SUPERSIDES.qname());

	private List<QName> targets = new ArrayList<>(defaultTargets); 

	public List<QName> targetAttributes() {
		return targets;
	}
}
