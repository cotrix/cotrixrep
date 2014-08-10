package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.NodeType.*;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Link;
import org.cotrix.domain.common.BeanContainer;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoContainer;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;

public class NeoCode extends NeoAttributed implements Code.Bean {

	public static final NeoStateFactory<Code.Bean> factory = new NeoStateFactory<Code.Bean>() {
		
		@Override
		public Code.Bean beanFrom(Node node) {
			return new NeoCode(node);
		}
		
		@Override
		public Node nodeFrom(Code.Bean state) {
			return new NeoCode(state).node();
		}
	};
	
	public NeoCode(Node node) {
		super(node);
	}
	
	public NeoCode(Code.Bean state) {
		
		super(CODE,state);
		
		for (Link.Bean l : state.links())
			node().createRelationshipTo(NeoLink.factory.nodeFrom(l),Relations.LINK);
	}
	
	@Override
	public Code.Private entity() {
		return new Code.Private(this);
	}

	@Override
	public BeanContainer<Link.Bean> links() {
		return new NeoContainer<>(node(), Relations.LINK, NeoLink.factory);
	}

}
