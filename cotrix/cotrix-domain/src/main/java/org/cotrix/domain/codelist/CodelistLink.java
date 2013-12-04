package org.cotrix.domain.codelist;

import org.cotrix.domain.po.CodelistLinkPO;
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

		
		
		



		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((targetId() == null) ? 0 : targetId().hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof CodelistLink.Private))
				return false;
			CodelistLink.Private other = (CodelistLink.Private) obj;
			if (targetId() == null) {
				if (other.targetId() != null)
					return false;
			} else if (!targetId().equals(other.targetId()))
				return false;
			return true;
		}
	}
}
