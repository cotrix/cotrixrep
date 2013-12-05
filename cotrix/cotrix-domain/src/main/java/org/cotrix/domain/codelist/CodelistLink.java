package org.cotrix.domain.codelist;

import org.cotrix.domain.memory.CodelistLinkMS;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.EntityProvider;

/**
 * An {@link Identified}, {@link Attributed}, {@link Named} link between {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface CodelistLink extends Identified, Attributed, Named {

	/**
	 * Returns the identifier of the target codelist.
	 * 
	 * @return the identifier
	 */
	String targetId();

	
	
	
	static interface State extends Named.State, EntityProvider<Private> {

		String targetId();

		void targetId(String id);
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
		public String targetId() {
			return state().targetId();
		}

		@Override
		public void update(CodelistLink.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			if (!state().targetId().equals(changeset.targetId()))
				state().targetId(changeset.targetId());
		}

		@Override
		public void build(CodelistLink.State state) {
			state.targetId(targetId());
		}

		@Override
		public CodelistLink.State copy() {
			
			CodelistLinkMS state = new CodelistLinkMS();
			
			build(state);
			
			return state;
		}

	}
}
