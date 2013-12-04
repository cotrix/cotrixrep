package org.cotrix.domain.trait;

import org.cotrix.domain.po.VersionedPO;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.version.Version;

/**
 * A domain object that has a version.
 * 
 * @author Fabio Simeoni
 */
public interface Versioned {

	/**
	 * Returns the current version of this object.
	 * 
	 * @return the version
	 */
	String version();
	
	
	static interface State<T extends Abstract<T>> extends Named.State<T> {
		
		void version(Version version);
		
		Version version();
	}

	/**
	 * {@link Named.Abstract} implementation of {@link Versioned}.
	 * <p>
	 * Yields copies with given versions. Copying is recursive over the object's dependencies and requires an
	 * {@link IdGenerator} for the generation of new identifiers.
	 * 
	 * @param <T> the concrete type of instances, hence of their (versioned) copies
	 */
	static abstract class Abstract<T extends Abstract<T>> extends Named.Abstract<T> implements Versioned {

		
		public Abstract(Versioned.State<T> state) {
			super(state);
		}
		
		@Override
		public abstract Versioned.State<T> state();
		
		@Override
		public String version() {
			return state().version() == null ? null : state().version().value();
		}

		protected void fillPO(boolean withId, VersionedPO<T> po) {

			super.fillPO(withId, po);

			if (state().version() != null)
				po.version(state().version());
		}

		@Override
		public void update(T changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			// version has changed?
			if (changeset.version() != null && !changeset.version().equals(this.version()))
				throw new IllegalArgumentException("cannot change the version (" + version() + ") of entity " + id()
						+ ". Versioning is performed by copy");
		};

		/**
		 * Returns a copy of this object with a new version and no identifiers.
		 * 
		 * @param version the new version.
		 * @return the versioned copy of this object
		 * 
		 * @throws IllegalArgumentException if the version is invalid for the underlying versioning scheme
		 * @throws IllegalStateException if the version does not follow the current version of this object according to
		 *             the underlying versioning scheme
		 */
		public T bump(String version) {

			Version newVersion = state().version().bumpTo(version);

			T copy = copyWith(newVersion);

			return copy;
		}

		protected abstract T copyWith(Version version);
		
	}
}
