package org.cotrix.domain.codelist;

import static org.cotrix.domain.dsl.Codes.*;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


public interface CodelistLink extends Identified, Attributed, Named {

	/**
	 * Returns the identifier of the target codelist.
	 * 
	 * @return the identifier
	 */
	Codelist target();
	
	/**
	 * Returns the type of the link.
	 * @return the type
	 */
	LinkType type();
		
	
	static interface State extends Identified.State, Attributed.State, Named.State, EntityProvider<Private> {

		Codelist.State target();
		
		LinkType type();
		
		void type(LinkType type);

		void target(Codelist.State state);
	}

	/**
	 * A {@link Named.Abstract} implementation of {@link CodelistLink}.
	 * 
	 */
	public class Private extends Named.Abstract<Private,State> implements CodelistLink {

		public Private(CodelistLink.State state) {
			super(state);
		}

		@Override
		public Codelist target() {
			return state().target() == null ? null : new Codelist.Private(state().target());
		}
		
		@Override
		public LinkType type() {
			return state().type();
		}

		@Override
		public void update(CodelistLink.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

	
			if (changeset.target()!=null) {
				Codelist.State target = reveal(changeset.target()).state();
				if (!state().target().equals(target))
					state().target(target);
			}
			
		}

	}
}
