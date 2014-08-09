package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.NodeType.*;
import static org.neo4j.graphdb.Direction.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class NeoCodelink extends NeoAttributed implements Codelink.Bean {

	public static final NeoStateFactory<Codelink.Bean> factory = new NeoStateFactory<Codelink.Bean>() {
		
		@Override
		public Codelink.Bean beanFrom(Node node) {
			return new NeoCodelink(node);
		}
		
		@Override
		public Node nodeFrom(Codelink.Bean state) {
			return new NeoCodelink(state).node();
		}
	};
	
	public NeoCodelink(Node node) {
		super(node);
	}
	
	public NeoCodelink(Codelink.Bean state) {

		super(CODELINK,state);	
		
		target(state.target());
		definition(state.definition());
		
	}
	
	@Override
	public QName qname() {
		LinkDefinition.State def = definition();
		return def==null?null:def.qname();
	}
	
	@Override
	public void qname(QName name) {
		throw new UnsupportedOperationException("codelink names are read-only");
	}
	
	public Codelink.Private entity() {
		return new Codelink.Private(this);
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
	public LinkDefinition.State definition() {
	
		Relationship rel = node().getSingleRelationship(Relations.INSTANCEOF,OUTGOING);
		
		//links should always have a type
		if (rel==null)
			throw new IllegalStateException(id()+" has an orphaned codelist link");
				
		return NeoLinkDefinition.factory.beanFrom(rel.getEndNode());
	}
	
	@Override
	public void definition(LinkDefinition.State state) {
		
		node().createRelationshipTo(resolve(state,LINKDEF),Relations.INSTANCEOF);
	}
	
}
