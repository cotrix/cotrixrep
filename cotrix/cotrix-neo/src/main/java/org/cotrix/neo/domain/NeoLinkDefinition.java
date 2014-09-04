package org.cotrix.neo.domain;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.dsl.Data.*;
import static org.cotrix.domain.utils.DomainConstants.*;
import static org.cotrix.domain.values.ValueFunctions.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;
import static org.neo4j.graphdb.Direction.*;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.links.LinkOfLink;
import org.cotrix.domain.links.LinkValueType;
import org.cotrix.domain.links.NameLink;
import org.cotrix.domain.values.ValueFunction;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class NeoLinkDefinition extends NeoDescribed implements LinkDefinition.Bean {

	public static final NeoStateFactory<LinkDefinition.Bean> factory = new NeoStateFactory<LinkDefinition.Bean>() {
		
		@Override
		public LinkDefinition.Bean beanFrom(Node node) {
			return new NeoLinkDefinition(node);
		}
		
		@Override
		public Node nodeFrom(LinkDefinition.Bean state) {
			return new NeoLinkDefinition(state).node();
		}
	};
	
	public NeoLinkDefinition(Node node) {
		super(node);
	}
	
	public NeoLinkDefinition(LinkDefinition.Bean state) {

		super(LINKDEF,state);	
		
		target(state.target());
		valueType(state.valueType());
		function(state.function());
		range(state.range());
		
		
	}
	
	public LinkDefinition.Private entity() {
		return new LinkDefinition.Private(this);
	}
	
	@Override
	public Codelist.Bean target() {
		
		Relationship rel = node().getSingleRelationship(Relations.LINK,OUTGOING);
		
		//links should always have a target
		if (rel==null)
			throw new IllegalStateException("orphaned codelist link "+qname()+" ("+id()+")");
		
		return NeoCodelist.factory.beanFrom(rel.getEndNode());
	}
	
	@Override
	public void target(Codelist.Bean bean) {
		
		Node target = resolve(bean, CODELIST);
		
		//allow orphan semantics
		if (target!=null)
			node().createRelationshipTo(target, Relations.LINK);
		
	}
	
	@Override
	public LinkValueType valueType() {
		
		//attribute-based: retrieve as blob
		if (node().hasProperty(type_prop))
			return (LinkValueType) binder().fromXML((String) node().getProperty(type_prop));
		
		//link-based: retrieve as link
		Relationship rel = node().getSingleRelationship(Relations.LOL,OUTGOING);
		
		if (rel!=null)
			return new LinkOfLink(NeoLinkDefinition.factory.beanFrom(rel.getEndNode()).entity());
		
		//name-based: return constant
		return NameLink.INSTANCE;
	}
	
	@Override
	public void valueType(LinkValueType state) {
		
		//name-based: store nothing  
		if (state==NameLink.INSTANCE) {
			removeValueType();
			return;
		}
		
		else
			
		//link-based: point to link as it can change over time
		if (state instanceof LinkOfLink) {
			
			LinkDefinition link = ((LinkOfLink) state).target();
			
			Node target = resolve(reveal(link).bean(),CODELINK);
			
			//allow orphan semantics
			if (target!=null)
				node().createRelationshipTo(target, Relations.LOL);
			
			return;
		}
		
		else {
		
			//attribute-based: store as blob, it won't change over time
			node().setProperty(type_prop,binder().toXML(state));
		
		}
	}
	
	private void removeValueType() {
		
		//attribute-based: retrieve as blob
		if (node().hasProperty(type_prop))
			node().removeProperty(type_prop);
		
		//link-based: retrieve as link
		Relationship rel = node().getSingleRelationship(Relations.LOL,OUTGOING);
		
		if (rel!=null)
			rel.delete();;
	}
	
	@Override
	public Range range() {
		
		return node().hasProperty(range_prop)? 
					(Range) binder().fromXML((String) node().getProperty(range_prop))
					:defaultRange;
	}
	
	@Override
	public void range(Range type) {
		
		if(type!=defaultRange)
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
