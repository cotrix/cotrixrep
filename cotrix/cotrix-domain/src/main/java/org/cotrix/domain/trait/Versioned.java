package org.cotrix.domain.trait;

import org.cotrix.domain.memory.VersionedMS;
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
	
	interface State extends Named.State {
		
		void version(Version version);
		
		Version version();
	}


	//private logic
	
	abstract class Abstract<SELF extends Abstract<SELF,S>,S extends State> extends Named.Abstract<SELF,S> implements Versioned {

		
		public Abstract(S state) {
			super(state);
		}
		
		
		@Override
		public String version() {
			return state().version() == null ? null : state().version().value();
		}

		
		protected void buildState(VersionedMS state) {

			super.buildState(state);

			if (state().version() != null)
				state.version(state().version());
		}

		@Override
		public void update(SELF changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			//detect illegal version changes
			if (changeset.version() != null && !changeset.version().equals(this.version()))
				throw new IllegalArgumentException("cannot change the version (" + version() + ") of entity " + id()
						+ ". Versioning is performed by copy");
		};
		

		public SELF bump(String version) {

			Version newVersion = state().version().bumpTo(version);

			SELF copy = copyWith(newVersion);

			return copy;
		}

		protected abstract SELF copyWith(Version version);
		
	}
}
