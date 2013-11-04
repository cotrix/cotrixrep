package org.cotrix.repository.memory;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.domain.trait.Identified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An in-memory store of domain objects.
 *  
 * @author Fabio Simeoni
 *
 */
public class MStore {

	private static Logger log = LoggerFactory.getLogger(MStore.class);
	
	Map<Class<?>,Map<String,?>> objects= new HashMap<Class<?>,Map<String,?>>();
	
	/**
	 * Adds an object to this store.
	 * @param object the object
	 * @param type the type of the object
	 */
	public <T extends Identified.Abstract<T>> void add(T object, Class<T> type) {

		notNull("element",object);

		if (object.isChangeset())
			throw new IllegalArgumentException(type.getSimpleName()+" instance is a changeset and cannot be added");
		
		String id = object.id();
		
		if (id==null)
			throw new AssertionError(type.getSimpleName()+" instance has no identifier");		
		else
			if (objectsOf(type).containsKey(id))
				throw new IllegalStateException(type.getSimpleName()+" instance is already persisted");
		
		objectsOf(type).put(object.id(),object);
		
		log.info("added {} ({})",object.getClass().getCanonicalName(),object.id());
		
	}
	
	/**
	 * Retrieves an object in this store.
	 * @param id the object identifier.
	 * @param type the type of the object.
	 * @return the object
	 */
	public <T> T lookup(String id, Class<T> type) {
		
		valid("the identifier of this "+type.getCanonicalName()+"'s instance",id);
		
		return objectsOf(type).get(id);
		
	}
	
	/**
	 * Updated an object in this store.
	 * @param changeset the set of changes to apply to the object
	 * @param type the type of the object
	 */
	public <T extends Identified.Abstract<T>> void update(T changeset, Class<T> type) {
		
		notNull("changeset",changeset);
		
		if (!changeset.isChangeset())
			throw new IllegalArgumentException(type.getSimpleName()+" instance is a not changeset and cannot be used to update");
		
		T current = lookup(changeset.id(),type);
		
		T pCurrent = reveal(current,type);
		
		pCurrent.update(changeset);
		
		log.info("updated {} ({})",type.getCanonicalName(),changeset.id());
		 
	}
	
	/**
	 * Removes an object from this store
	 * @param id the identifier of the object
	 * @param type the type of the object
	 */
	public void remove(String id, Class<?> type) {
		
		valid("the identifier of this "+type.getSimpleName()+"'s instance",id);
		
		notNull("type",type);
		
		if (objectsOf(type).remove(id)==null)
			throw new IllegalStateException("unkown object "+id+" of type "+type);
		
		log.info("removed {} ({})",type.getCanonicalName(),id);
	}
	
	/**
	 * Returns all the objects in this store that have a given type.
	 * @param type the type of the objects
	 * @return the objects of the given type
	 */
	public <T> List<T> getAll(Class<T> type) {
		
		return new ArrayList<T>(objectsOf(type).values());
	}
	
	//helper
	private <T> Map<String,T> objectsOf(Class<T> type) {
		
		@SuppressWarnings("all")
		Map<String,T> map = (Map) objects.get(type);
		
		if (map==null) {
			map =   new HashMap<String,T>();
			objects.put(type,map);
		}
		
		return map;
	}
}
