package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.Relations.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.neo.domain.Constants.NodeType;
import org.cotrix.neo.domain.utils.NeoContainer;
import org.neo4j.graphdb.Node;

public class NeoAttributed extends NeoNamed implements Attributed.Bean {

	public NeoAttributed(Node node) {
		super(node);
	}
	
	public NeoAttributed(NodeType type,Attributed.Bean bean) {
		
		super(type,bean);
		
		for (Attribute.Bean a : bean.attributes())
			node().createRelationshipTo(NeoAttribute.factory.nodeFrom(a),ATTRIBUTE);
		
	}

	@Override
	public BeanContainer<Attribute.Bean> attributes() {
		return new NeoContainer<>(node(),ATTRIBUTE,NeoAttribute.factory);
	}
	


}
