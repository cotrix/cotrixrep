package org.cotrix.repository.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;

import org.cotrix.common.Utils;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Query;
import org.cotrix.repository.Repository;
import org.cotrix.repository.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class MemoryRepository<S extends Identified.Abstract<S>> implements Repository<S> {

	private static Logger log = LoggerFactory.getLogger(MemoryRepository.class);
	
	private final IdGenerator generator;
	
	private final Map<String,S> objects = new HashMap<String,S>();
	
	public MemoryRepository(IdGenerator generator) {
		this.generator=generator;
	}
	
	protected String generateId() {
		return generator.id();
	}
	
	@Override
	public void add(S object) {
		
		object.setId(generator.id());
		
		objects.put(object.id(),object);
	}
	
	@Override
	public S lookup(String id) {
		return objects.get(id);
	}
	
	@Override
	public void remove(String id) {
		
		if (objects.remove(id)==null)
			throw new IllegalStateException("object "+id+" is unknown, hence cannot be removed.");
		
	}
	
	public void update(S changeset) {
		
		S current = lookup(changeset.id());
		
		if (current==null)
			throw new IllegalStateException("object "+changeset.id()+" is unknown, hence cannot be updated.");
		
		current.update(changeset);
	};
	
	
	@Override
	public <R> Iterable<R> queryFor(Query<S,R> query) {
		
		return reveal(query).execute(this);
	}
	
	public List<S> getAll() {
		return new ArrayList<S>(objects.values());
	}
	
	@Override
	public <R> R get(Specification<S, R> specs) {
		return reveal(specs).execute(this);
	}
	
	@Override
	public int size() {
		return objects.size();
	}
	
	public void clear(@Observes Startup event) {
		log.info("clearing "+this);
		objects.clear();
	}
	
	//helpers
	
	@SuppressWarnings("all")
	private <R> MQuery<S,R> reveal(Query<S,R> query) {
		return (MQuery<S,R>) Utils.reveal(query,MQuery.class);
	}
	
	@SuppressWarnings("all")
	private <R> MSpecification<S,R> reveal(Specification<S,R> query) {
		return (MSpecification<S,R>) Utils.reveal(query,MSpecification.class);
	}
}
