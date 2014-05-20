package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Attribute.Private;
import org.cotrix.domain.attributes.Attribute.State;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;

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
		
		name(state.name());
		type(state.type());
		value(state.value());
		language(state.language());
		
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
	public Private entity() {
		return new Attribute.Private(this);
	}
}
