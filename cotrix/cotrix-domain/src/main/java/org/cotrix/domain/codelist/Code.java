package org.cotrix.domain.codelist;

import org.cotrix.domain.common.Container;
import org.cotrix.domain.po.CodePO;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;

/**
 * An {@link Identified}, {@link Attributed}, and {@link Named} symbol.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Code extends Identified,Attributed,Named {
	
	/**
	 * Returns the {@link Codelink}s of this code.
	 * @return the links
	 */
	Container<? extends Codelink> links();
	
	/**
	 * A {@link Named.Abstract} implementation of {@link Code}.
	 */
	public class Private extends Named.Abstract<Private> implements Code {

		private Container.Private<Codelink.Private> links;
		
		////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Creates an instance with given parameters.
		 * @param params the parameters
		 */
		public Private(CodePO params) {
			super(params);
			this.links=params.links();
		}
		
		@Override
		public Container.Private<Codelink.Private> links() {
			return links;
		}
		
		//fills PO for copy/versioning purposes
		protected void fillPO(boolean withId, CodePO po) {
			super.fillPO(withId,po);
			po.setLinks(links.copy(withId));
		}
		
		public Private copy(boolean withId) {
			CodePO po = new CodePO(withId?id():null);
			fillPO(withId,po);
			return new Private(po);
		}
		
		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			this.links.update(changeset.links());
		}

		@Override
		public String toString() {
			return "Code [id="+id()+", name=" + name() + ", attributes=" + attributes()+"]" ;
		}
	}
}