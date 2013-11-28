package org.cotrix.domain.codelist;

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

	
	/**
	 * A {@link Versioned.Abstract} implementation of {@link Codelist}.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public class Private extends Versioned.Abstract<Private> implements Codelist {
		
		private final Container.Private<Code.Private> codes;
		private final Container.Private<CodelistLink.Private> links;

		/**
		 * Creates a new instance from a given set of parameters.
		 * @param params the parameters
		 */
		public Private(CodelistPO param) {
			super(param);
			this.codes = param.codes();
			this.links = param.links();
		}

		@Override
		public Container.Private<Code.Private> codes() {
			return codes;
		}
		
		@Override
		public Container<CodelistLink.Private> links() {
			return links;
		}

		protected void buildPO(boolean withId,CodelistPO po) {
			super.fillPO(withId,po);
			po.setCodes(codes().copy(withId));
			po.setLinks(links.copy(withId));
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
				po.setVersion(version);

			return new Private(po);
		}

		@Override
		public String toString() {
			return "Codelist [id="+id()+", name=" + name() + ", codes=" + codes + ", attributes=" + attributes() + ", version="
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
			result = prime * result + ((codes == null) ? 0 : codes.hashCode());
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
			if (codes == null) {
				if (other.codes != null)
					return false;
			} else if (!codes.equals(other.codes))
				return false;
			return true;
		}
		
	}
}