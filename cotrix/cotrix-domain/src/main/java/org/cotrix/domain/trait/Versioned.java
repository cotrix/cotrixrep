package org.cotrix.domain.trait;

import org.cotrix.domain.version.Version;

/**
 * A domain object that has a version.
 * 
 * @author Fabio Simeoni
 */
public interface Versioned {

	//public read-only interface
	
	/**
	 * Returns the current version of this object.
	 * 
	 * @return the version
	 */
	String version();
	
	
	//private state interface
	
	interface State {
		
		Version version();
		
		void version(Version version);
	}


	//private logic
	
	abstract class Abstract<SELF extends Abstract<SELF,S>,S extends State & Identified.Bean & Attributed.Bean & Named.Bean> extends Named.Abstract<SELF,S> implements Versioned {

		public Abstract(S state) {
			super(state);
		}
		
		
		@Override
		public String version() {
			return bean().version() == null ? null : bean().version().value();
		}

		
		@Override
		public void update(SELF changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			//detect illegal version changes
			if (changeset.version() != null && !changeset.version().equals(this.version()))
				throw new IllegalArgumentException("cannot change the version (" + version() + ") of entity " + id()
						+ ". Versioning is performed by copy");
		};
		
		public void build(S state) {
			state.version(bean().version());
		};
		

		public SELF bump(String version) {

			Version newVersion = bean().version().bumpTo(version);

			SELF copy = copyWith(newVersion);

			return copy;
		}

		protected abstract SELF copyWith(Version version);
		
	}
}
