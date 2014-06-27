package org.cotrix.neo.repository;

import static org.cotrix.common.Constants.*;
import static org.cotrix.neo.NeoNodeFactory.*;
import static org.cotrix.neo.domain.Constants.NodeType.*;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.Codelist.State;
import org.cotrix.neo.NeoUtils;
import org.cotrix.neo.domain.NeoCodelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.spi.StateRepository;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped @Alternative @Priority(RUNTIME)
public class NeoCodelistRepository implements StateRepository<Codelist.State> {

	private static Logger log = LoggerFactory.getLogger(NeoCodelistRepository.class);
	
	@Inject
	private GraphDatabaseService store;
	
	
	@Inject
	private NeoCodelistQueries queries;
	
	
	@Override
	public void add(State list) {
		
		try {
			new NeoCodelist(list);
		}
		finally {
			log.info("cleared thread cache after import");
			NeoUtils.threadCache().clear();
		}
	}

	@Override
	public boolean contains(String id) {
		
		return node(CODELIST,id)!=null;

	}

	@Override
	public State lookup(String id) {
		
		Node node = node(CODELIST,id);

		return node == null? null : new NeoCodelist(node);
	}

	@Override
	public void remove(String id) {
		
		//no need to check for null, infrastructure ensures codelist exists
		try {
			NeoUtils.removeEntityNode(node(CODELIST,id));
		}
		catch(IllegalStateException e) {
			throw new CodelistRepository.UnremovableCodelistException("cannot remove codelist: other codelists link to it",e);
		}
		catch(Exception e) {
			throw new RuntimeException("cannot remove codelist",e);
		}
		
	}
	

	@Override
	public int size() {
		return queries.repositorySize(CODELIST).execute();
	}

}
