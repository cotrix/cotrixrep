package org.cotrix.domain.codelist;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;

/**
 * An {@link Identified} and {@link Attributed} instance of a {@link CodelistLink}.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Codelink extends Identified, Attributed {

	/**
	 * Returns the definition of this link.
	 * 
	 * @return the definition
	 */
	CodelistLink type();

	/**
	 * Returns the identifier of the target of this link.
	 * 
	 * @return the target identifier
	 */
	String target();

	static interface State extends Identified.State, Attributed.State, EntityProvider<Private> {

		CodelistLink.State type();

		String target();

		void target(String id);
	}

	/**
	 * An {@link Attributed.Abstract} implementation of {@link Codelink}.
	 * 
	 */
	public class Private extends Attributed.Abstract<Private, State> implements Codelink {

		public Private(Codelink.State state) {
			super(state);
		}

		@Override
		public String target() {
			return state().target();
		}

		@Override
		public CodelistLink.Private type() {
			return new CodelistLink.Private(state().type());
		}

		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			type().update(changeset.type());

			if (!target().equals(changeset.target()))
				state().target(changeset.target());
		}

	}
}
