package org.cotrix.domain.codelist;

import org.cotrix.domain.po.CodeLinkPO;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;



/**
 * An {@link Identified} and {@link Attributed} instance of a {@link CodelistLink}.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codelink extends Identified,Attributed {
	
	/**
	 * Returns the definition of this link. 
	 * @return the definition
	 */
	CodelistLink definition();

	/**
	 * Returns the identifier of the target of this link.
	 * @return the target identifier
	 */
	String targetId();
	
	
	
	
	/**
	 * An {@link Attributed.Abstract} implementation of {@link Codelink}.
	 * 
	 */
	public class Private extends Attributed.Abstract<Private> implements Codelink {
		
		private final CodelistLink.Private definition;
		private String targetId;
		
		/**
		 * Creates a new instance with given parameters
		 * @param params the parameters
		 */
		public Private(CodeLinkPO params) {
			
			super(params);
			
			this.targetId = params.targetId();
			this.definition=params.definition();
		}

		
		@Override
		public String targetId() {
			return targetId;
		}
		
		@Override
		public CodelistLink.Private definition() {
			return definition;
		}
		
		@Override
		public void update(Private changeset) throws IllegalArgumentException ,IllegalStateException {
			
			super.update(changeset);
			
			this.definition.update(changeset.definition());
			
			if (!targetId.equals(changeset.targetId()))
				targetId=changeset.targetId();
		}
		
		//fills PO for copy/versioning purposes
		protected void fillPO(boolean withId,CodeLinkPO po) {
			
			super.fillPO(withId,po);
			po.setDefinition(definition.copy(withId));
			po.setTargetId(targetId);
			
		}
		
		public Private copy(boolean withId) {
			CodeLinkPO po = new CodeLinkPO(withId?id():null);
			fillPO(withId,po);
			return new Private(po);
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((definition == null) ? 0 : definition.hashCode());
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
			if (definition == null) {
				if (other.definition != null)
					return false;
			} else if (!definition.equals(other.definition))
				return false;
			if (targetId == null) {
				if (other.targetId != null)
					return false;
			} else if (!targetId.equals(other.targetId))
				return false;
			return true;
		}
	
	}
}

