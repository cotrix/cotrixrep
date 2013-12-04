package org.cotrix.domain.codelist;

import static org.cotrix.domain.dsl.Codes.*;

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

		
		@Override
		public Container.Private<Code.Private,Code.State> codes() {
			return container(state().codes());
		}
		
		@Override
		public Container.Private<CodelistLink.Private,CodelistLink.State> links() {
			return container(state().links());
		}

		protected void buildPO(boolean withId,CodelistPO po) {
			super.fillPO(withId,po);
			po.codes(codes().copy(withId).state());
			po.links(links().copy(withId).state());
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

		
		
		


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((codes() == null) ? 0 : codes().hashCode());
			result = prime * result + ((links() == null) ? 0 : links().hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof Codelist.Private))
				return false;
			Codelist.Private other = (Codelist.Private) obj;
			if (codes() == null) {
				if (other.codes() != null)
					return false;
			} else if (!codes().equals(other.codes()))
				return false;
			if (links() == null) {
				if (other.links() != null)
					return false;
			} else if (!links().equals(other.links()))
				return false;
			return true;
		}
	}
}