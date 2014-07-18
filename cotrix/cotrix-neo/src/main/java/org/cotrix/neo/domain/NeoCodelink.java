package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.NodeType.*;
import static org.neo4j.graphdb.Direction.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class NeoCodelink extends NeoAttributed implements Codelink.State {

	public static final NeoStateFactory<Codelink.State> factory = new NeoStateFactory<Codelink.State>() {
		
		@Override
		public Codelink.State beanFrom(Node node) {
			return new NeoCodelink(node);
		}
		
		@Override
		public Node nodeFrom(Codelink.State state) {
			return new NeoCodelink(state).node();
		}
	};
	
	public NeoCodelink(Node node) {
		super(node);
	}
	
	public NeoCodelink(Codelink.State state) {

		super(CODELINK,state);	
		
		target(state.target());
		type(state.type());
		
	}
	
	@Override
	public QName name() {
		LinkDefinition.State type = type();
		return type==null?null:type.name();
	}
	
	@Override
	public void name(QName name) {
		throw new UnsupportedOperationException("codelink names are read-only");
	}
	
	public Codelink.Private entity() {
		return new Codelink.Private(this);
	}
	
	@Override
	public Code.State target() {
		
		Relationship rel = node().getSingleRelationship(Relations.LINK,OUTGOING);
		
		return rel==null  ? null : NeoCode.factory.beanFrom(rel.getEndNode());
				
	}
	
	@Override
	public void target(Code.State state) {
		
		Relationship rel = node().getSingleRelationship(Relations.LINK,OUTGOING);
		
		//'at most one' semantics
		if (rel!=null)
			rel.delete();
		
		node().createRelationshipTo(resolve(state,CODE), Relations.LINK);
	}
	
	@Override
	public LinkDefinition.State type() {
	
		Relationship rel = node().getSingleRelationship(Relations.INSTANCEOF,OUTGOING);
		
		//links should always have a type
		if (rel==null)
			throw new IllegalStateException(id()+" has an orphaned codelist link");
				
		return NeoCodelistLink.factory.beanFrom(rel.getEndNode());
	}
	
	@Override
	public void type(LinkDefinition.State state) {
		
		node().createRelationshipTo(resolve(state,CODELISTLINK),Relations.INSTANCEOF);
	}
	
}
