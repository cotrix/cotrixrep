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
		return QName.valueOf((String) node().getProperty(name_prop));
	}

	@Override
	public void name(QName name) {
		node().setProperty(name_prop,name.toString());
	}

	
}
