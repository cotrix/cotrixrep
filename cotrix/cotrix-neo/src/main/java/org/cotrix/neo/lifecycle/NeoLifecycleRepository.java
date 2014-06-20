package org.cotrix.neo.lifecycle;

import static org.cotrix.common.Constants.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;
import static org.neo4j.tooling.GlobalGraphOperations.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.Utils;
import org.cotrix.lifecycle.Lifecycle;
import org.cotrix.lifecycle.State;
import org.cotrix.lifecycle.impl.LifecycleRepository;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

@Singleton @Alternative @Priority(RUNTIME)
public class NeoLifecycleRepository implements LifecycleRepository {
	
	private static Logger log = LoggerFactory.getLogger(NeoLifecycleRepository.class);
	
	private static XStream stream = new XStream(); 
	
	@Inject
	private GraphDatabaseService store;
	
	@Override
	public void add(Lifecycle lc) {
		
		Node node = store.createNode(LIFECYCLE);
		
		node.setProperty(name_prop,lc.name());
		node.setProperty(id_prop,lc.resourceId());
		node.setProperty(state_prop,stream.toXML(lc.state()));
	}

	@Override
	public ResumptionToken lookup(String id) {
		
		Node node = nodeFor(id);

		if (node==null)
			return null;
		
		return tokenFrom(node);
	}
	
	@Override
	public Map<String, ResumptionToken> lookup(Collection<String> ids) {
		
		Map<String,ResumptionToken> tokens = new HashMap<>();
		
		try (
				ResourceIterator<Node> retrieved = at(store).getAllNodesWithLabel(LIFECYCLE).iterator(); 
			) 
		{
			while (retrieved.hasNext()) {
				Node node = retrieved.next();
				tokens.put((String)node.getProperty(id_prop), tokenFrom(node));
			}
				
		}
		
		return tokens;
	}

	@Override
	public void update(Lifecycle lc) {
		
		Node node = nodeFor(lc.resourceId());

		if (node==null)
			throw new AssertionError("attempt to update transient lifecycle "+lc.resourceId());
		
		node.setProperty(state_prop,stream.toXML(lc.state()));
		
		log.info("updated {}'s lifecycle",lc.resourceId());
	}
	
	@Override
	public void delete(String id) {
		
		Node node = nodeFor(id);

		if (node==null)
			throw new AssertionError("attempt to update transient lifecycle "+id);
		
		try {
			node.delete();
		}
		catch(Exception e) {
			Utils.rethrow("cannot delete codelist lifecycle for "+id,e);
		}
		
		log.info("deleted {}'s lifecycle",id);
		
	}
	
	private ResumptionToken tokenFrom(Node node) {
		
		String name = (String) node.getProperty(name_prop);
		State state = (State) stream.fromXML((String)node.getProperty(state_prop));
		
		return new ResumptionToken(name, state);
	}
	
	
	//helpers
	
	private Node nodeFor(String id) {
		
		try (
				ResourceIterator<Node> retrieved = store.findNodesByLabelAndProperty(LIFECYCLE,id_prop,id).iterator(); 
			) 
		{
			return retrieved.hasNext()? retrieved.next(): null;
		}
		
	}

}
