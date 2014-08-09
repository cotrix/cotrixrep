package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.Relations.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Attribute.Bean;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.neo.domain.Constants.NodeType;
import org.cotrix.neo.domain.utils.NeoContainer;
import org.neo4j.graphdb.Node;

public class NeoAttributed extends NeoIdentified implements Attributed.Bean {

	public NeoAttributed(Node node) {
		super(node);
	}
	
	public <S extends Identified.Bean & Attributed.Bean> 
			NeoAttributed(NodeType type,S state) {
		
		super(type,state);
		
		for (Attribute.Bean a : state.attributes())
			node().createRelationshipTo(NeoAttribute.factory.nodeFrom(a),ATTRIBUTE);
		
	}

	@Override
	public BeanContainer<Bean> attributes() {
		return new NeoContainer<Bean>(node(),ATTRIBUTE,NeoAttribute.factory);
	}
	


}
