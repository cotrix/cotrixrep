package org.cotrix.domain.primitive.container;

import static org.cotrix.domain.trait.Change.*;
import static org.cotrix.domain.utils.Utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cotrix.domain.primitive.entity.Entity;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Change;
import org.cotrix.domain.trait.Mutable;

/**
 * A {@link Mutable} {@link Container}.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of contained objects
 */
public class MutableContainer<T extends Entity<T>> extends AbstractContainer<T> implements Mutable<Container<T>> {

	private Change change;

	
	/**
	 * Creates an instance that contain given entities.
	 * 
	 * @param objects the entities
	 */
	public MutableContainer(List<T> objects) {
		notNull(objects);

		for (T object : objects)
			add(object);
		
	}
	
	@Override
	public Change change() {
		return change;
	}

	@Override
	public boolean isDelta() {
		return change != null;
	}

	@Override
	public void reset() {
		setChange(null);
	}

	public void setChange(Change change) {

		notNull(change);

		this.change = change;

	}

	@Override
	public void update(Container<T> delta) {
		
		Map<String, T> index = indexObjects();

		for (T object : delta) {
	
			String id = object.id();

			if (index.containsKey(id)) {
				
				switch (object.change()) {
					case DELETED:
						objects().remove(index.remove(id));
						break;
					case MODIFIED:
						index.get(id).update(object);
						break;

				} 
			}
			//add case
			else {
				object.reset();
				add(object);
			}

		}	
	}

	private Map<String, T> indexObjects() {

		Map<String, T> index = new HashMap<String, T>();

		for (T object : this)
			index.put(object.id(), object);

		return index;
	}

	protected boolean add(T object) throws IllegalArgumentException {

		notNull(object);
		
		propagateChangeFrom(object);
		
		return objects().add(object);
		
	}
	
	@Override
	public MutableContainer<T> copy(IdGenerator generator) {
		
		List<T> copied = new ArrayList<T>();
		for (T object : this)
			copied.add(object.copy(generator));
		
		return new MutableContainer<T>(copied); 
		
	}
	
	// helper
	protected void propagateChangeFrom(T object) throws IllegalArgumentException {

		// redundant checks, but clearer

		// first time: inherit NEW or MODIFIED
		if (object.isDelta() && !this.isDelta())
			this.setChange(object.change() == NEW ? NEW : MODIFIED);

		// other times: if not another NEW, MODIFIED
		if (object.isDelta() && this.isDelta())
			if (object.change() != this.change)
				this.setChange(MODIFIED);

		if (this.isDelta() && !object.isDelta())
			throw new IllegalArgumentException("object is " + this.change + " and can only contain other changes");

	}

}
