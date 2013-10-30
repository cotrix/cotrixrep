package org.cotrix.domain;

import java.util.List;

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
		
		//concessions to ORM that knows not how to map Container<T> even if we use it here with a concrete type...
		//this also forces us to make field attributes non-final....
		@SuppressWarnings("all")
		private void setORMLinks(List<Codelink.Private> links) {
			if (links!=null) //no idea why ORM arrives with NULL before it arrives with not-NULL value
				this.links = new Container.Private<Codelink.Private>(links);
		}
		
		@SuppressWarnings("all")
		private List<Codelink.Private> getORMLinks() {
			return links.objects();
		}
		
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
		public void update(Private delta) throws IllegalArgumentException, IllegalStateException {
			super.update(delta);
			this.links.update(delta.links());
		}

		@Override
		public String toString() {
			return "Code [id="+id()+", name=" + name() + ", attributes=" + attributes()+"]" ;
		}
	}
}