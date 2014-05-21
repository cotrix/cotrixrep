package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;
import static org.neo4j.graphdb.Direction.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Attribute.Private;
import org.cotrix.domain.attributes.Attribute.State;
import org.cotrix.domain.attributes.AttributeType;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class NeoAttribute extends NeoIdentified implements Attribute.State {

	public static final NeoStateFactory<Attribute.State> factory = new NeoStateFactory<Attribute.State>() {
		
		@Override
		public State beanFrom(Node node) {
			return new NeoAttribute(node);
		}
		
		@Override
		public Node nodeFrom(State state) {
			return new NeoAttribute(state).node();
		}
	};
	
	public NeoAttribute(Node node) {
		super(node);
	}

	public NeoAttribute(Attribute.State state) {
		
		super(ATTRIBUTE,state);
		
		attributeType(state.attributeType());
		value(state.value());	
	}
	
	@Override
	public AttributeType.State attributeType() {
		
		Relationship rel = node().getSingleRelationship(Relations.INSTANCEOF,OUTGOING);
		
		//links should always have a type
		if (rel==null)
			throw new IllegalStateException(id()+" has an orphaned attribute type link");
				
		return NeoAttributeType.factory.beanFrom(rel.getEndNode());
	}

	@Override
	public void attributeType(AttributeType.State state) {
		
		//allow for private types
		Node node = softResolve(state, ATTRIBUTE_TYPE);
			
		if (node==null)
			node = NeoAttributeType.factory.nodeFrom(state);
			
		node().createRelationshipTo(node,Relations.INSTANCEOF);
		
	}
	
	@Override
	public QName name() {
		return attributeType().name();
	}

	@Override
	public void name(QName name) {
		attributeType().name(name);
	}

	@Override
	public QName type() {
		
		return attributeType().type();
	}

	@Override
	public void type(QName type) {
		
		attributeType().type(type);
		
	}

	@Override
	public String value() {
		return (String) node().getProperty(value_prop,null);
	}

	@Override
	public void value(String value) {
		
		if (value==null)
			node().removeProperty(value_prop);
		else
			node().setProperty(value_prop,value);
		
	}

	@Override
	public String language() {
		return attributeType().language();
	}

	@Override
	public void language(String language) {
		
		attributeType().language(language);
		
	}

	@Override
	public Private entity() {
		return new Attribute.Private(this);
	}
}
