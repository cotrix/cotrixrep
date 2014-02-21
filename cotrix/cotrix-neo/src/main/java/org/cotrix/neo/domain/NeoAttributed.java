package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.Relations.*;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Attribute.State;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.neo.domain.Constants.NodeType;
import org.cotrix.neo.domain.utils.NeoContainer;
import org.neo4j.graphdb.Node;

public class NeoAttributed extends NeoIdentified implements Attributed.State {

	public NeoAttributed(Node node) {
		super(node);
	}
	
	public <S extends Identified.State & Attributed.State> 
			NeoAttributed(NodeType type,S state) {
		
		super(type,state);
		
		for (Attribute.State a : state.attributes())
			node().createRelationshipTo(NeoAttribute.factory.nodeFrom(a),ATTRIBUTE);
		
	}

	@Override
	public NamedStateContainer<State> attributes() {
		return new NeoContainer<State>(node(),ATTRIBUTE,NeoAttribute.factory);
	}
	


}
