package org.cotrix.neo.domain;

import static org.cotrix.neo.NeoUtils.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;
import static org.neo4j.graphdb.Direction.*;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.Codelist.State;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.codelist.LinkType;
import org.cotrix.domain.links.NameLink;
import org.cotrix.neo.NeoNodeFactory;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

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
		
		Relationship rel = node().getSingleRelationship(Relations.LINK,OUTGOING);
		
		//links should always have a target
		if (rel==null)
			throw new IllegalStateException("link is dangling");
		
		return NeoCodelist.factory.beanFrom(rel.getEndNode());
	}
	
	@Override
	public void target(State state) {
		
		Relationship rel = node().getSingleRelationship(Relations.LINK, OUTGOING);
		
		//'exactly one' semantics 
		if (rel!=null)
			rel.delete();
		
		Node target = NeoNodeFactory.node(CODELIST,state.id());
		
		if (target==null)
			throw new IllegalStateException("link is dangling");
		
		node().createRelationshipTo(target, Relations.LINK);
		
	}
	
	@Override
	public LinkType type() {
		
		return node().hasProperty(type_prop)? 
					(LinkType) binder().fromXML((String) node().getProperty(type_prop))
					: NameLink.INSTANCE;
	}
	
	@Override
	public void type(LinkType type) {
		
		if(type()!=NameLink.INSTANCE)
			node().setProperty(type_prop,binder().toXML(type));
		
	}
	
}
