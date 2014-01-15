package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Named;
import org.cotrix.neo.domain.Constants.NodeType;
import org.neo4j.graphdb.Node;

public class NeoNamed extends NeoAttributed implements Named.State {


	public NeoNamed(Node node) {
		super(node);
	}
	
	public <S extends Named.State & Attributed.State> NeoNamed(NodeType type, S state) {
		super(type,state);
		
		name(state.name());
		
	}
	
	@Override
	public QName name() {
		
		String val = (String) node().getProperty(name_prop,null);
		
		return val==null? null:QName.valueOf(val);
		
	}

	@Override
	public void name(QName name) {
		
		if (name==null)
			node().removeProperty(name_prop);
		else
			node().setProperty(name_prop,name.toString());
	}

	
}
