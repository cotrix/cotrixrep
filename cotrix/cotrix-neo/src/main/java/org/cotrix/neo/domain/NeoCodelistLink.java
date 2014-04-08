package org.cotrix.neo.domain;

import static org.cotrix.domain.links.ValueFunctions.*;
import static org.cotrix.neo.NeoUtils.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;
import static org.neo4j.graphdb.Direction.*;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.Codelist.State;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.links.OccurrenceRange;
import org.cotrix.domain.links.OccurrenceRanges;
import org.cotrix.domain.links.ValueFunction;
import org.cotrix.domain.links.ValueType;
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
		valueType(state.valueType());
		function(state.function());
		range(state.range());
		
		
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
		
		Node target = node(state);
		
		//allow dangling semantics
		if (target!=null)
			node().createRelationshipTo(target, Relations.LINK);
		
	}
	
	@Override
	public ValueType valueType() {
		
		return node().hasProperty(type_prop)? 
					(ValueType) binder().fromXML((String) node().getProperty(type_prop))
					: NameLink.INSTANCE;
	}
	
	@Override
	public void valueType(ValueType type) {
		
		if(type!=NameLink.INSTANCE)
			node().setProperty(type_prop,binder().toXML(type));
		
	}
	
	@Override
	public OccurrenceRange range() {
		
		return node().hasProperty(range_prop)? 
					(OccurrenceRange) binder().fromXML((String) node().getProperty(range_prop))
					:OccurrenceRanges.arbitrarily;
	}
	
	@Override
	public void range(OccurrenceRange type) {
		
		if(type!=OccurrenceRanges.arbitrarily)
			node().setProperty(range_prop,binder().toXML(type));
		
	}
	
	@Override
	public ValueFunction function() {
		
		return node().hasProperty(function_prop)? 
					(ValueFunction) binder().fromXML((String) node().getProperty(function_prop))
					: identity;
	}
	
	@Override
	public void function(ValueFunction type) {
		
		if(type!=identity)
			node().setProperty(function_prop,binder().toXML(type));
		
	}
	
}
