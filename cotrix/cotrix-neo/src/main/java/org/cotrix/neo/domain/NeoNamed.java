package org.cotrix.neo.domain;

import static org.cotrix.neo.domain.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Named;
import org.cotrix.neo.domain.Constants.NodeType;
import org.neo4j.graphdb.Node;

public class NeoNamed extends NeoIdentified implements Named.Bean {


	public NeoNamed(Node node) {
		super(node);
	}
	
	public NeoNamed(NodeType type, Named.Bean bean) {
		
		super(type,bean);
		
		qname(bean.qname());
		
	}
	
	@Override
	public QName qname() {
		
		String val = (String) node().getProperty(name_prop,null);
		
		return val==null? null:QName.valueOf(val);
		
	}

	@Override
	public void qname(QName name) {
		
		if (name==null)
			node().removeProperty(name_prop);
		else
			node().setProperty(name_prop,name.toString());
	}

	
}
