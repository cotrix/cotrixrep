package org.cotrix.domain.codelist;

import static org.cotrix.domain.dsl.Codes.*;

import java.util.Collection;

import org.cotrix.domain.common.Container;
import org.cotrix.domain.po.CodePO;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.EntityProvider;

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
	
	
	static interface State extends Named.State,EntityProvider<Private> {
		
		Collection<Codelink.State> links();
		
		void links(Collection<Codelink.State> links);
		
	}
	
	/**
	 * A {@link Named.Abstract} implementation of {@link Code}.
	 */
	public class Private extends Named.Abstract<Private,State> implements Code {

		public Private(Code.State state) {
			
			super(state);
		
		}
		
		
		@Override
		public Container.Private<Codelink.Private,Codelink.State> links() {
			
			return container(state().links());
			
		}
		
		//fills PO for copy/versioning purposes
		protected void fillPO(boolean withId, CodePO po) {
			
			super.fillPO(withId,po);
			po.links(links().copy(withId).state());
			
		}
		
		public Private copy(boolean withId) {

			CodePO po = new CodePO(withId?id():null);
			fillPO(withId,po);
			return new Private(po);
			
		}
		
		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			links().update(changeset.links());
		}

		@Override
		public String toString() {
			return "Code [id="+id()+", name=" + name() + ", attributes=" + attributes()+"]" ;
		}

	}
}