package org.cotrix.domain.codelist;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.po.CodelistPO;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.Version;



/**
 * An {@link Identified}, {@link Attribute}, {@link Named}, and {@link Versioned} set of {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codelist extends Identified,Attributed,Named,Versioned {

	/**
	 * Returns the codes of this list.
	 * @return the codes
	 */
	Container<? extends Code> codes();
	
	/**
	 * Returns the links of this list.
	 * @return the links.
	 */
	Container<? extends CodelistLink> links();
	
	
	static interface State extends Versioned.State {
	
		Collection<Code.State> codes();
		
		void codes(Collection<Code.State> codes);
		
		
		Collection<CodelistLink.State> links();
		
		void links(Collection<CodelistLink.State> links);
		
	}
	
	/**
	 * A {@link Versioned.Abstract} implementation of {@link Codelist}.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public class Private extends Versioned.Abstract<Private,State> implements Codelist {
		
		/**
		 * Creates a new instance with a given state.
		 * @param state the state
		 */
		public Private( Codelist.State state) {
			super(state);
		}

		
		private static Container.Provider<Code.Private,Code.State> codeProvider = new Container.Provider<Code.Private,Code.State>() {
			@Override
			public Code.Private objectFor(Code.State state) {
				return new Code.Private(state);
			}
			@Override
			public Code.State stateOf(Code.Private s) {
				return s.state();
			}
		};
		
		private static Container.Provider<CodelistLink.Private,CodelistLink.State> linkProvider = new Container.Provider<CodelistLink.Private,CodelistLink.State>() {
			@Override
			public CodelistLink.Private objectFor(CodelistLink.State state) {
				return new CodelistLink.Private(state);
			}
			@Override
			public CodelistLink.State stateOf(CodelistLink.Private s) {
				return s.state();
			}
		};
		
		@Override
		public Container.Private<Code.Private,Code.State> codes() {
			return new Container.Private<Code.Private,Code.State>(state().codes(),codeProvider);
		}
		
		@Override
		public Container.Private<CodelistLink.Private,CodelistLink.State> links() {
			return new Container.Private<CodelistLink.Private,CodelistLink.State>(state().links(),linkProvider);
		}

		protected void buildPO(boolean withId,CodelistPO po) {
			super.fillPO(withId,po);
			po.codes(new ArrayList<Code.State>(codes().copy(withId).objects()));
			po.links(new ArrayList<CodelistLink.State>(links().copy(withId).objects()));
		}

		@Override
		public Private copy(boolean withId) {
			CodelistPO po = new CodelistPO(withId?id():null);
			buildPO(withId,po);
			return new Private(po);
		}
		
		@Override
		protected final Private copyWith(Version version) {
			
			CodelistPO po = new CodelistPO(null);
			buildPO(false,po);
			
			if (version!=null)
				po.version(version);

			return new Private(po);
		}

		@Override
		public String toString() {
			return "Codelist [id="+id()+", name=" + name() + ", codes=" + codes() + ", attributes=" + attributes() + ", version="
					+ version() + (status()==null?"":" ("+status()+") ")+"]";
		}

		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			this.codes().update(changeset.codes());
		}

	}
}