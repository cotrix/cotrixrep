package org.cotrix.domain.common;

import static org.cotrix.common.CommonUtils.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;

/**
 * An immutable and typed collection of domain entities.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T>
 *            the type of entities
 */
public interface Container<T> extends Iterable<T> {

	// public read-only interface

	// (no implication entities are all in memory)

	/**
	 * Returns the number of entities in this container.
	 * 
	 * @return the number of entities
	 */
	int size();

	/**
	 * Returns <code>true</code> if this container contains a given entity.
	 * 
	 * @param entity
	 *            the entity
	 * @return <code>true</code> if this container contains the given entity
	 */
	// broader than T, because we return Container<? extends T> to clients and
	// yet must allow contains(T)
	// we return Container<? extends T> because we use Container<T.Private> in
	// implementations
	boolean contains(Object entity);

	/**
	 * Returns <code>true</code> if this container contains a given entity.
	 * 
	 * @param entity
	 *            the entity identifier
	 * @return <code>true</code> if this container contains the given entity
	 */
	boolean contains(String id);

	/**
	 * Returns a given entity in this container.
	 * 
	 * @param id
	 *            the entity identifier
	 * @return the entity
	 * @throws IllegalStateException
	 *             if no entity with the given identifier is in this container
	 */
	T lookup(String id) throws IllegalStateException;

	// private domain logic
	// we use this base class for both generic and named entities

	abstract class Abstract<T extends Identified.Abstract<T, S>,
	// type of state beans, must be able to return their wrappers
	S extends Identified.State & EntityProvider<T>,
	// this is to return the state beans to subclasses
	// we hide it from clients by instantiating it in subclasses
	C extends StateContainer<S>> implements Container<T> {

		private final C state;

		public Abstract(C state) {

			notNull("state", state);

			this.state = state;

		}

		@Override
		public Iterator<T> iterator() {
			return new IteratorAdapter<T, S>(state.iterator());
		}

		@Override
		public boolean contains(Object entity) {

			notNull("entity", entity);

			// unwrap and delegate

			return entity instanceof Identified.Abstract ?

			state().contains(reveal(entity, Identified.Abstract.class).state()) : false;
		};

		@Override
		public boolean contains(String id) throws IllegalStateException {

			return !state().get(Collections.singleton(id)).isEmpty();
		}
		
		@Override
		public T lookup(String id) throws IllegalStateException {

			Collection<S> matches = state().get(Collections.singleton(id));

			if (matches.isEmpty())
				throw new IllegalStateException("no entity " + id + " in this container");

			return matches.iterator().next().entity();
		}
		

		@Override
		public int size() {
			return state.size();
		}

		public void update(Abstract<T, S, C> changeset) {

			Map<String, T> updates = new HashMap<String, T>();

			for (T entityChangeset : changeset) {

				String id = entityChangeset.id();

				if (state.contains(id))

					if (entityChangeset.status() == null)
						throw new IllegalArgumentException("invalid changeset:" + entityChangeset.id() + " cannot be added twice");

					else
						switch (entityChangeset.status()) {

						case DELETED:
							state.remove(id);
							break;

						case MODIFIED: // accumulate updates
							updates.put(entityChangeset.id(), entityChangeset);
							break;

						}

				else
					state.add(entityChangeset.state());

			}

			// process updates
			if (!updates.isEmpty())
				for (S toUpdate : state.get(updates.keySet()))
					toUpdate.entity().update(updates.get(toUpdate.id()));

		}

		public C state() {
			return state;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			return result;
		}

		@Override
		@SuppressWarnings("all")
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Abstract))
				return false;
			Abstract other = (Abstract) obj;
			if (state == null) {
				if (other.state != null)
					return false;
			} else if (!state.equals(other.state))
				return false;
			return true;
		}

		@Override
		public String toString() {
			final int maxLen = 100;
			return "[" + (state != null ? toString(maxLen) : null) + "]";
		}

		private String toString(int maxLen) {
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			int i = 0;
			for (Iterator<?> iterator = this.iterator(); iterator.hasNext() && i < maxLen; i++) {
				if (i > 0)
					builder.append(", ");
				builder.append(iterator.next());
			}
			builder.append("]");
			return builder.toString();
		}

	}

	// derive to reduce parameters

	class Private<T extends Identified.Abstract<T, S>, S extends Identified.State & EntityProvider<T>> extends Abstract<T, S, StateContainer<S>> {

		public Private(StateContainer<S> state) {

			super(state);

		}

	}

}
