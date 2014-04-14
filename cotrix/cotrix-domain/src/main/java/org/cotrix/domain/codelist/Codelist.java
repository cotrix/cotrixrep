package org.cotrix.domain.codelist;

import static org.cotrix.domain.dsl.Codes.namedContainer;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.memory.CodelistMS;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.Version;



/**
 * An {@link Identified}, {@link Attribute}, {@link Named}, and {@link Versioned} set of {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codelist extends Identified,Attributed,Named,Versioned {

	//public read-only interface
	
	/**
	 * Returns the codes of this list.
	 * @return the codes
	 */
	NamedContainer<? extends Code> codes();
	
	/**
	 * Returns the links of this list.
	 * @return the links.
	 */
	NamedContainer<? extends CodelistLink> links();
	
	
	//private state interface
	
	interface State extends Identified.State, Attributed.State, Named.State, Versioned.State, EntityProvider<Private> {
	
		NamedStateContainer<Code.State> codes();
		
		NamedStateContainer<CodelistLink.State> links();
		
	}
	
	//private logic
	
	final class Private extends Versioned.Abstract<Private,State> implements Codelist {
		
		public Private( Codelist.State state) {
			super(state);
		}

		
		@Override
		public NamedContainer.Private<Code.Private,Code.State> codes() {
			return namedContainer(state().codes());
		}
		
		@Override
		public NamedContainer.Private<CodelistLink.Private,CodelistLink.State> links() {
			return namedContainer(state().links());
		}

		@Override
		protected final Private copyWith(Version version) {
			
			Codelist.State state = new CodelistMS(state());
			
			state.version(version);

			return state.entity();
		}

		@Override
		public String toString() {
			return "Codelist [id="+id()+", name=" + name() + ", codes=" + codes() + ", attributes=" + attributes() + ", links=" + links() + ", version="
					+ version() + (status()==null?"":" ("+status()+") ")+"]";
		}

		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			links().update(changeset.links());
			
			codes().update(changeset.codes());
		
		}
		
	}
}