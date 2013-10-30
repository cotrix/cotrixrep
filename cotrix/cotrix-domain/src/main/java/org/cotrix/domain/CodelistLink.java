package org.cotrix.domain;

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
public interface CodelistLink extends Identified, Attributed,Named {
	
	/**
	 * Returns the identifier of the target codelist.
	 * @return the identifier
	 */
	String targetId();
	
	
	/**
	 * A {@link Named.Abstract} implementation of {@link CodelistLink}.
	 * 
	 */
	public class Private extends Named.Abstract<Private> implements CodelistLink {
		
		private String targetId;

		/**
		 * Creates an instance with given parameters.
		 * @param params the parameters
		 */
		public Private(CodelistLinkPO params) {
			super(params);
			this.targetId = params.targetId();	
		}

		@Override
		public String targetId() {
			return targetId;
		}
		
		@Override
		public void update(CodelistLink.Private delta) throws IllegalArgumentException, IllegalStateException {
			
			super.update(delta);
			
			if (!targetId.equals(delta.targetId()))
				targetId=delta.targetId();
		}
		
		//fills PO for copy/versioning purposes
		private final void fillPO(boolean withId,CodelistLinkPO po) {
			super.fillPO(withId,po);
			po.setTargetId(targetId);
		}
		
		@Override
		public Private copy(boolean withId) {
			CodelistLinkPO po = new CodelistLinkPO(withId?id():null);
			this.fillPO(withId,po);
			return new Private(po);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((targetId == null) ? 0 : targetId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Private other = (Private) obj;
			if (targetId == null) {
				if (other.targetId != null)
					return false;
			} else if (!targetId.equals(other.targetId))
				return false;
			return true;
		}

	}
}
