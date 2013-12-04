package org.cotrix.domain.codelist;

import org.cotrix.domain.memory.CodelinkMS;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.EntityProvider;

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
	CodelistLink definition();

	/**
	 * Returns the identifier of the target of this link.
	 * 
	 * @return the target identifier
	 */
	String targetId();

	static interface State extends Attributed.State, EntityProvider<Private> {

		CodelistLink.State definition();

		String targetId();

		void targetId(String id);
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
		public String targetId() {
			return state().targetId();
		}

		@Override
		public CodelistLink.Private definition() {
			return new CodelistLink.Private(state().definition());
		}

		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			definition().update(changeset.definition());

			if (!targetId().equals(changeset.targetId()))
				state().targetId(changeset.targetId());
		}


		public Private copy(boolean withId) {
			
			CodelinkMS state = new CodelinkMS(withId ? id() : null);
			
			super.buildState(withId, state);
			
			state.definition(definition().copy(withId));
			state.targetId(targetId());
			
			return new Private(state);
		}

	}
}
