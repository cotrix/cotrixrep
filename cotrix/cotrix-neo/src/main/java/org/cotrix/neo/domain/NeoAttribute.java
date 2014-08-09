package org.cotrix.neo.domain;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;
import static org.neo4j.graphdb.Direction.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Attribute.Private;
import org.cotrix.domain.attributes.Attribute.Bean;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.attributes.Facet;
import org.cotrix.neo.domain.Constants.Relations;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class NeoAttribute extends NeoIdentified implements Attribute.Bean {

	public static final NeoStateFactory<Attribute.Bean> factory = new NeoStateFactory<Attribute.Bean>() {
		
		@Override
		public Bean beanFrom(Node node) {
			return new NeoAttribute(node);
		}
		
		@Override
		public Node nodeFrom(Bean state) {
			return new NeoAttribute(state).node();
		}
	};
	
	public NeoAttribute(Node node) {
		super(node);
	}

	public NeoAttribute(Attribute.Bean state) {
		
		super(ATTRIBUTE,state);
		
		definition(state.definition());
		value(state.value());	
	}
	
	@Override
	public AttributeDefinition.Bean definition() {
		
		if (node().hasProperty(cdef_prop))
			return commonDefinitionFor((String) node().getProperty(cdef_prop)).state();
		
		Relationship rel = node().getSingleRelationship(Relations.INSTANCEOF,OUTGOING);
				
		//links should always have a type
		if (rel==null)
			throw new IllegalStateException(id()+" has an orphaned definition link");
				
		return NeoAttributeDefinition.factory.beanFrom(rel.getEndNode());

	}

	@Override
	public void definition(AttributeDefinition.Bean state) {
		
		//common definitions are named in a property and then reconsituted in memory
		if (isCommon(state.qname().getLocalPart()))
			node().setProperty(cdef_prop,state.qname().getLocalPart());
		
		//other definition are linked to
		else  {
			
			Node target = null;
			
			if (state.isShared())
				target = softResolve(state, ATTRDEF);
			
			if (target==null)
				target =NeoAttributeDefinition.factory.nodeFrom(state);
			
			node().createRelationshipTo(target,Relations.INSTANCEOF);
		
		}
	}
	
	@Override
	public QName qname() {
		return definition().qname();
	}

	@Override
	public void qname(QName name) {
		definition().qname(name);
	}

	@Override
	public QName type() {
		
		return definition().type();
	}
	
	@Override
	public boolean is(QName name) {
		return definition().is(name);
	}
	
	@Override
	public boolean is(Facet facet) {
		return definition().is(facet);
	}
	
	@Override
	public void type(QName type) {
		
		definition().type(type);
		
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
	public String description() {
		return (String) node().getProperty(description_prop,null);
	}

	@Override
	public void description(String value) {
		
		if (value==null)
			node().removeProperty(description_prop);
		else
			node().setProperty(description_prop,value);
		
	}

	@Override
	public String language() {
		return definition().language();
	}

	@Override
	public void language(String language) {
		
		definition().language(language);
		
	}

	@Override
	public Private entity() {
		return new Attribute.Private(this);
	}
}
