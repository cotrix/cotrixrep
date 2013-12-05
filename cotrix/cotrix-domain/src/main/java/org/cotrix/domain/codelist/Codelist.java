package org.cotrix.domain.codelist;

import static org.cotrix.domain.dsl.Codes.*;

import java.util.Collection;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Container;
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
	Container<? extends Code> codes();
	
	/**
	 * Returns the links of this list.
	 * @return the links.
	 */
	Container<? extends CodelistLink> links();
	
	
	//private state interface
	
	interface State extends Versioned.State, EntityProvider<Private> {
	
		Collection<Code.State> codes();
		
		void codes(Collection<Code.State> codes);
		
		
		Collection<CodelistLink.State> links();
		
		void links(Collection<CodelistLink.State> links);
		
	}
	
	//private logic
	
	final class Private extends Versioned.Abstract<Private,State> implements Codelist {
		
		public Private( Codelist.State state) {
			super(state);
		}

		
		@Override
		public Container.Private<Code.Private,Code.State> codes() {
			return container(state().codes());
		}
		
		@Override
		public Container.Private<CodelistLink.Private,CodelistLink.State> links() {
			return container(state().links());
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
			
			codes().update(changeset.codes());
		}

	}
}