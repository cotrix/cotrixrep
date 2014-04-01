package org.cotrix.domain.codelist;

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
		
	
	static interface State extends Identified.State, Attributed.State, Named.State, EntityProvider<Private> {

		Codelist target();

		void target(Codelist id);
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
			return state().target();
		}

		@Override
		public void update(CodelistLink.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			Codelist target = changeset.target();
			if (target!=null && !state().target().equals(target))
				state().target(target);
		}

	}
}
