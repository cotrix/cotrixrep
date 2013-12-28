package org.cotrix.neo.repository;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.Codelist.State;
import org.cotrix.repository.Query;
import org.cotrix.repository.impl.StateRepository;
import org.neo4j.graphdb.GraphDatabaseService;

@Alternative @Priority(RUNTIME)
public class NeoCodelistRepository implements StateRepository<Codelist.State> {

	@Inject
	GraphDatabaseService store;
	
	
	@Override
	public void add(State object) {
		
		// TODO Auto-generated method stub

	}

	@Override
	public boolean contains(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public State lookup(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <R> R get(Query<State, R> query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
