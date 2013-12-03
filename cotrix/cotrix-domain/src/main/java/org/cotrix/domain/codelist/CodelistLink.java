package org.cotrix.domain.codelist;

import org.cotrix.domain.po.CodelistLinkPO;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;

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

	static interface State extends Named.State<CodelistLink.Private> {

		String targetId();

		void targetId(String id);
	}

	/**
	 * A {@link Named.Abstract} implementation of {@link CodelistLink}.
	 * 
	 */
	public class Private extends Named.Abstract<Private> implements CodelistLink {

		private final CodelistLink.State state;

		/**
		 * Creates an instance with given parameters.
		 * 
		 * @param params the parameters
		 */
		public Private(CodelistLink.State state) {
			this.state = state;
		}

		@Override
		public CodelistLink.State state() {
			return state;
		}

		@Override
		public String targetId() {
			return state.targetId();
		}

		@Override
		public void update(CodelistLink.Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			if (!state.targetId().equals(changeset.targetId()))
				state.targetId(changeset.targetId());
		}

		// fills PO for copy/versioning purposes
		private final void fillPO(boolean withId, CodelistLinkPO po) {
			super.fillPO(withId, po);
			po.targetId(targetId());
		}

		@Override
		public Private copy(boolean withId) {
			CodelistLinkPO po = new CodelistLinkPO(withId ? id() : null);
			this.fillPO(withId, po);
			return new Private(po);
		}

	}
}
