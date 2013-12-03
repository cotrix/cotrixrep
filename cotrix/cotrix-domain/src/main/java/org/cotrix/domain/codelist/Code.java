package org.cotrix.domain.codelist;

import java.util.ArrayList;
import java.util.Collection;

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
	
	
	static interface State extends Named.State<Code.Private> {
		
		Collection<Codelink.State> links();
		
		void links(Collection<Codelink.State> links);
		
	}
	
	/**
	 * A {@link Named.Abstract} implementation of {@link Code}.
	 */
	public class Private extends Named.Abstract<Private> implements Code {

		private static Container.Provider<Codelink.Private,Codelink.State> provider = new Container.Provider<Codelink.Private,Codelink.State>() {
			@Override
			public Codelink.Private objectFor(Codelink.State state) {
				return new Codelink.Private(state);
			}
			@Override
			public Codelink.State stateOf(Codelink.Private s) {
				return s.state();
			}
		};
		
		private final Code.State state;
		
		public Private(Code.State state) {
			this.state=state;
		}
		
		@Override
		public Code.State state() {
			return state;
		}
		
		@Override
		public Container.Private<Codelink.Private,Codelink.State> links() {
			return new Container.Private<Codelink.Private, Codelink.State>(state().links(),provider);
		}
		
		//fills PO for copy/versioning purposes
		protected void fillPO(boolean withId, CodePO po) {
			super.fillPO(withId,po);
			po.links(new ArrayList<Codelink.State>(links().copy(withId).objects()));
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