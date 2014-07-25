package org.cotrix.neo.domain;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.attributes.Facet;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.values.ValueType;
import org.cotrix.neo.domain.utils.NeoStateFactory;
import org.neo4j.graphdb.Node;

public class NeoDefinition extends NeoIdentified implements AttributeDefinition.State {

	public static final NeoStateFactory<AttributeDefinition.State> factory = new NeoStateFactory<AttributeDefinition.State>() {
		
		@Override
		public AttributeDefinition.State beanFrom(Node node) {
			return new NeoDefinition(node);
		}
		
		@Override
		public Node nodeFrom(AttributeDefinition.State state) {
			return new NeoDefinition(state).node();
		}
	};
	
	public NeoDefinition(Node node) {
		super(node);
	}

	public NeoDefinition(AttributeDefinition.State state) {
		
		super(ATTRDEF,state);
		
		qname(state.qname());
		type(state.type());
		language(state.language());
		valueType(state.valueType());
		range(state.range());
		shared(state.isShared());
		
	}
	
	@Override
	public boolean isShared() {
		
		return (Boolean) node().getProperty(shared_prop,true);
		
	}
	
	
	void shared(boolean flag) {
		
		//store only the less likely case
		if (!flag)
			node().setProperty(shared_prop,flag);
	}
	
	
	@Override
	public QName qname() {
		return QName.valueOf((String) node().getProperty(name_prop));
	}

	@Override
	public void qname(QName name) {
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
	public boolean is(QName name) {
		return type().equals(name);
	}
	
	@Override
	public boolean is(Facet facet) {
		//temporarily only on common defs, supported by default on domain defs
		return !isCommon(qname()) || isCommon(qname(),facet);
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
		
		return (ValueType) binder().fromXML((String) node().getProperty(vtype_prop));
		
	}
	
	@Override
	public void valueType(ValueType state) {
		
		node().setProperty(vtype_prop,binder().toXML(state));
		
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
	public AttributeDefinition.Private entity() {
		return new AttributeDefinition.Private(this);
	}
}
