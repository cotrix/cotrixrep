package org.cotrix.neo.repository;

import static org.cotrix.common.Constants.*;
import static org.cotrix.neo.domain.Constants.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.Codelist.State;
import org.cotrix.neo.NeoUtils;
import org.cotrix.neo.domain.NeoCodelist;
import org.cotrix.repository.spi.StateRepository;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

@ApplicationScoped @Alternative @Priority(RUNTIME)
public class NeoCodelistRepository implements StateRepository<Codelist.State> {

	@Inject
	private GraphDatabaseService store;
	
	
	@Inject
	private NeoCodelistQueries queries;
	
	
	@Override
	public void add(State list) {
		
		new NeoCodelist(list);
		
	}

	@Override
	public boolean contains(String id) {
		
		return nodeFor(id)!=null;

	}

	@Override
	public State lookup(String id) {
		
		Node node = nodeFor(id);

		return node == null? null : new NeoCodelist(node);
	}

	@Override
	public void remove(String id) {
		
		//no need to check for null, infrastructure ensures codelist exists
		NeoUtils.remove(nodeFor(id));
	}
	

	@Override
	public int size() {
		return queries.repositorySize().execute();
	}
	
	
	//helpers
	
	private Node nodeFor(String id) {
		
		try (
			ResourceIterator<Node> retrieved = store.findNodesByLabelAndProperty(CODELIST,id_prop,id).iterator(); 
		) 
		{
			return retrieved.hasNext()? retrieved.next(): null;
		}
		
	}

}
