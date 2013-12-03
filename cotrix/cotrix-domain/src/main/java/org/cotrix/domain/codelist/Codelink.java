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
	
	
	static interface State extends Attributed.State<Codelink.Private> {
		
		CodelistLink.State definition();
		
		String targetId();
		
		void targetId(String id);
	}
	
	
	/**
	 * An {@link Attributed.Abstract} implementation of {@link Codelink}.
	 * 
	 */
	public class Private extends Attributed.Abstract<Private> implements Codelink {
		
		private final Codelink.State state;
		
		public Private(Codelink.State state) {
			
			this.state=state;
		}

		@Override
		public Codelink.State state() {
			return state;
		}
		
		@Override
		public String targetId() {
			return state.targetId();
		}
		
		@Override
		public CodelistLink.Private definition() {
			return new CodelistLink.Private(state.definition());
		}
		
		@Override
		public void update(Private changeset) throws IllegalArgumentException ,IllegalStateException {
			
			super.update(changeset);
			
			definition().update(changeset.definition());
			
			if (!targetId().equals(changeset.targetId()))
				state.targetId(changeset.targetId());
		}
		
		//fills PO for copy/versioning purposes
		protected void fillPO(boolean withId,CodeLinkPO po) {
			
			super.fillPO(withId,po);
			po.definition(definition().copy(withId));
			po.targetId(targetId());
			
		}
		
		public Private copy(boolean withId) {
			CodeLinkPO po = new CodeLinkPO(withId?id():null);
			fillPO(withId,po);
			return new Private(po);
		}
	}
}

