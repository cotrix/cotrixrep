package org.cotrix.repository.impl.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Observes;

import org.cotrix.common.Utils;
import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Identified;
import org.cotrix.repository.Query;
import org.cotrix.repository.Repository;
import org.cotrix.repository.utils.UuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class MemoryRepository<S extends Identified.Abstract<S>> implements Repository<S> {

	private static Logger log = LoggerFactory.getLogger(MemoryRepository.class);
	
	private final IdGenerator generator = new UuidGenerator();
	
	private final Map<String,S> objects = new HashMap<String,S>();
	
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
	public <R> R get(Query<S,R> query) {
		
		return reveal(query).execute(this);
	}
	
	public List<S> getAll() {
		return new ArrayList<S>(objects.values());
	}
	
	@Override
	public int size() {
		return objects.size();
	}
	
	public void clear(@Observes Shutdown event) {
		log.info("cleaning "+this.getClass().getCanonicalName());
		objects.clear();
	}
	
	//helpers
	
	@SuppressWarnings("all")
	private <R> MQuery<S,R> reveal(Query<S,R> query) {
		return (MQuery<S,R>) Utils.reveal(query,MQuery.class);
	}
}
