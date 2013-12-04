package org.cotrix.domain.codelist;

import org.cotrix.domain.po.CodeLinkPO;
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

		// fills PO for copy/versioning purposes
		protected void fillPO(boolean withId, CodeLinkPO po) {

			super.fillPO(withId, po);
			po.definition(definition().copy(withId));
			po.targetId(targetId());

		}

		public Private copy(boolean withId) {
			CodeLinkPO po = new CodeLinkPO(withId ? id() : null);
			fillPO(withId, po);
			return new Private(po);
		}

		

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((definition() == null) ? 0 : definition().hashCode());
			result = prime * result + ((targetId() == null) ? 0 : targetId().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof Codelink.Private ))
				return false;
			Codelink.Private other = (Codelink.Private ) obj;
			if (definition() == null) {
				if (other.definition() != null)
					return false;
			} else if (!definition().equals(other.definition()))
				return false;
			if (targetId() == null) {
				if (other.targetId() != null)
					return false;
			} else if (!targetId().equals(other.targetId()))
				return false;
			return true;
		}
		
		

	}
}
