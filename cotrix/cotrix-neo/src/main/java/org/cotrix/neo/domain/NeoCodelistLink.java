package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.NodeType.*;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.Codelist.State;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

public class NeoCodelistLink extends NeoNamed implements CodelistLink.State {

	public static final NeoStateFactory<CodelistLink.State> factory = new NeoStateFactory<CodelistLink.State>() {
		
		@Override
		public CodelistLink.State beanFrom(Node node) {
			return new NeoCodelistLink(node);
		}
		
		@Override
		public Node nodeFrom(CodelistLink.State state) {
			return new NeoCodelistLink(state).node();
		}
	};
	
	public NeoCodelistLink(Node node) {
		super(node);
	}
	
	public NeoCodelistLink(CodelistLink.State state) {

		super(CODELISTLINK,state);	
		
		target(state.target());
		
	}
	
	public CodelistLink.Private entity() {
		return new CodelistLink.Private(this);
	}
	
	@Override
	public Codelist.State target() {

		Node node = node().getSingleRelationship(Relations.LINK,Direction.OUTGOING).getEndNode();
		return NeoCodelist.factory.beanFrom(node);
	}
	
	@Override
	public void target(State state) {
		
		node().createRelationshipTo(NeoCodelist.factory.nodeFrom(state), Relations.LINK);
		
	}
	
}
