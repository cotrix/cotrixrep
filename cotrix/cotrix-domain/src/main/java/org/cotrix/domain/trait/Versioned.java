package org.cotrix.domain.trait;

import static org.cotrix.common.Utils.*;

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

	/**
	 * {@link Named.Abstract} implementation of {@link Versioned}.
	 * <p>
	 * Yields copies with given versions. Copying is recursive over the object's dependencies and requires an
	 * {@link IdGenerator} for the generation of new identifiers.
	 * 
	 * @param <T> the concrete type of instances, hence of their (versioned) copies
	 */
	public abstract class Abstract<T extends Abstract<T>> extends Named.Abstract<T> implements Versioned {

		private final Version version;

		/**
		 * Creates a new instance from a given set of parameters.
		 * 
		 * @param params the parameters
		 */
		public Abstract(VersionedPO params) {
			super(params);
			this.version = params.version();
		}

		@Override
		public String version() {
			return version.value();
		}

		protected void fillPO(IdGenerator generator, VersionedPO po) {
			super.fillPO(generator, po);
			po.setVersion(version);
		}

		public void update(T delta) throws IllegalArgumentException, IllegalStateException {

			super.update(delta);

			// name has changed?
			if (!delta.version().equals(this.version()))
				throw new IllegalArgumentException("cannot change the version (" + version() + ") of entity " + id()
						+ ". Versioning is performed by copy");
		};

		public T bump(IdGenerator generator, String version) {

			notNull("version", version);

			if (id() == null)
				throw new IllegalStateException("object cannot be versioned because it has no identifier");

			Version newVersion = this.version.bumpTo(version);

			return copy(generator, newVersion);
		}

		@Override
		public T copy(IdGenerator generator) {
			return copy(generator, version);
		}

		protected abstract T copy(IdGenerator generator, Version version);

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((version == null) ? 0 : version.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Versioned.Abstract<?> other = (Versioned.Abstract<?>) obj;
			if (version == null) {
				if (other.version != null)
					return false;
			} else if (!version.equals(other.version))
				return false;
			return true;
		}

	}
}
