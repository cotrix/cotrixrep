package org.cotrix.neo.security;

import static org.cotrix.common.Constants.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.security.impl.NativeRealm;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

@Singleton @Alternative @Priority(RUNTIME)
public class NeoRealm extends NativeRealm {
	
	@Inject
	private GraphDatabaseService db;
	
	
	@Override
	protected String passwordFor(String name) {
		
		try 
		(
		 ResourceIterator<Node> it = db.findNodesByLabelAndProperty(IDENTITY,name_prop, name).iterator()
		) 
		{
			return it.hasNext() ? (String) it.next().getProperty(pwd_prop): null;
		}

		
	
	}
	
	@Override
	protected void create(String name, String pwd) {
		
		Node identity = db.createNode(IDENTITY);
		identity.setProperty(name_prop,name);
		identity.setProperty(pwd_prop,pwd);
					
	}
	
}
