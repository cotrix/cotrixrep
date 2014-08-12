package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.NodeType.*;
import static org.neo4j.graphdb.Direction.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class NeoLink extends NeoAttributed implements Link.Bean {

	public static final NeoStateFactory<Link.Bean> factory = new NeoStateFactory<Link.Bean>() {
		
		@Override
		public Link.Bean beanFrom(Node node) {
			return new NeoLink(node);
		}
		
		@Override
		public Node nodeFrom(Link.Bean state) {
			return new NeoLink(state).node();
		}
	};
	
	public NeoLink(Node node) {
		super(node);
	}
	
	public NeoLink(Link.Bean state) {

		super(CODELINK,state);	
		
		target(state.target());
		definition(state.definition());
		
	}
	
	@Override
	public QName qname() {
		LinkDefinition.Bean def = definition();
		return def==null?null:def.qname();
	}
	
	@Override
	public void qname(QName name) {
		//overrides super to avoid adding a property that should remain virtual
	}
	
	public Link.Private entity() {
		return new Link.Private(this);
	}
	
	@Override
	public Code.Bean target() {
		
		Relationship rel = node().getSingleRelationship(Relations.LINK,OUTGOING);
		
		return rel==null  ? null : NeoCode.factory.beanFrom(rel.getEndNode());
				
	}
	
	@Override
	public void target(Code.Bean state) {
		
		Relationship rel = node().getSingleRelationship(Relations.LINK,OUTGOING);
		
		//'at most one' semantics
		if (rel!=null)
			rel.delete();
		
		node().createRelationshipTo(resolve(state,CODE), Relations.LINK);
	}
	
	@Override
	public LinkDefinition.Bean definition() {
	
		Relationship rel = node().getSingleRelationship(Relations.INSTANCEOF,OUTGOING);
		
		//links should always have a type
		if (rel==null)
			throw new IllegalStateException(id()+" has an orphaned codelist link");
				
		return NeoLinkDefinition.factory.beanFrom(rel.getEndNode());
	}
	
	@Override
	public void definition(LinkDefinition.Bean state) {
		
		node().createRelationshipTo(resolve(state,LINKDEF),Relations.INSTANCEOF);
	}
	
}
