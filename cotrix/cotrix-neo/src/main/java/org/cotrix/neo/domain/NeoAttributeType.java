package org.cotrix.neo.domain;

import static org.cotrix.neo.NeoUtils.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.AttributeType;
import org.cotrix.domain.attributes.AttributeValueType;
import org.cotrix.domain.common.OccurrenceRange;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;

public class NeoAttributeType extends NeoIdentified implements AttributeType.State {

	public static final NeoStateFactory<AttributeType.State> factory = new NeoStateFactory<AttributeType.State>() {
		
		@Override
		public AttributeType.State beanFrom(Node node) {
			return new NeoAttributeType(node);
		}
		
		@Override
		public Node nodeFrom(AttributeType.State state) {
			return new NeoAttributeType(state).node();
		}
	};
	
	public NeoAttributeType(Node node) {
		super(node);
	}

	public NeoAttributeType(AttributeType.State state) {
		
		super(ATTRIBUTE_TYPE,state);
		
		name(state.name());
		type(state.type());
		language(state.language());
		valueType(state.valueType());
		range(state.range());
		
	}
	
	@Override
	public QName name() {
		return QName.valueOf((String) node().getProperty(name_prop));
	}

	@Override
	public void name(QName name) {
		node().setProperty(name_prop,name.toString());
	}

	@Override
	public QName type() {
		
		String val = (String) node().getProperty(type_prop,null);
		
		return val==null? null:QName.valueOf(val);
	}

	@Override
	public void type(QName type) {
		
		if (type==null)
			node().removeProperty(type_prop);
		else
			node().setProperty(type_prop,type.toString());
		
	}

	@Override
	public String language() {
		return (String) node().getProperty(lang_prop,null);
	}

	@Override
	public void language(String language) {
		
		if (language==null)
			node().removeProperty(lang_prop);
		else
			node().setProperty(lang_prop,language);
		
	}

	@Override
	public AttributeValueType valueType() {
		
		return (AttributeValueType) binder().fromXML((String) node().getProperty(attr_type_prop));
		
	}
	
	@Override
	public void valueType(AttributeValueType state) {
		
		node().setProperty(attr_type_prop,binder().toXML(state));
		
	}
	
	@Override
	public OccurrenceRange range() {
		
		return (OccurrenceRange) binder().fromXML((String) node().getProperty(range_prop));
		
	}
	
	@Override
	public void range(OccurrenceRange type) {
		
		node().setProperty(range_prop,binder().toXML(type));
		
	}
	
	@Override
	public AttributeType.Private entity() {
		return new AttributeType.Private(this);
	}
}
