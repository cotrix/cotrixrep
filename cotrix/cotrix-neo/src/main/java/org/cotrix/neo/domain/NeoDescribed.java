package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.Relations.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.trait.Described;
import org.cotrix.neo.domain.Constants.NodeType;
import org.cotrix.neo.domain.utils.NeoContainer;
import org.neo4j.graphdb.Node;

public class NeoDescribed extends NeoNamed implements Described.Bean {

	public NeoDescribed(Node node) {
		super(node);
	}
	
	public NeoDescribed(NodeType type,Described.Bean bean) {
		
		super(type,bean);
		
		for (Attribute.Bean a : bean.attributes())
			node().createRelationshipTo(NeoAttribute.factory.nodeFrom(a),ATTRIBUTE);
		
	}

	@Override
	public Container.Bean<Attribute.Bean> attributes() {
		return new NeoContainer<>(node(),ATTRIBUTE,NeoAttribute.factory);
	}
	


}
