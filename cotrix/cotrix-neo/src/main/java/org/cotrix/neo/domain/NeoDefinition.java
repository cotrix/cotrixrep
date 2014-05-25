package org.cotrix.neo.domain;

import static org.cotrix.neo.NeoUtils.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.values.ValueType;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;

public class NeoDefinition extends NeoIdentified implements Definition.State {

	public static final NeoStateFactory<Definition.State> factory = new NeoStateFactory<Definition.State>() {
		
		@Override
		public Definition.State beanFrom(Node node) {
			return new NeoDefinition(node);
		}
		
		@Override
		public Node nodeFrom(Definition.State state) {
			return new NeoDefinition(state).node();
		}
	};
	
	public NeoDefinition(Node node) {
		super(node);
	}

	public NeoDefinition(Definition.State state) {
		
		super(DEFINITION,state);
		
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
	public ValueType valueType() {
		
		return (ValueType) binder().fromXML((String) node().getProperty(attr_type_prop));
		
	}
	
	@Override
	public void valueType(ValueType state) {
		
		node().setProperty(attr_type_prop,binder().toXML(state));
		
	}
	
	@Override
	public Range range() {
		
		return (Range) binder().fromXML((String) node().getProperty(range_prop));
		
	}
	
	@Override
	public void range(Range type) {
		
		node().setProperty(range_prop,binder().toXML(type));
		
	}
	
	@Override
	public Definition.Private entity() {
		return new Definition.Private(this);
	}
}
