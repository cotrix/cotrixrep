package org.cotrix.domain;

import org.cotrix.domain.po.CodebagPO;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Versioned;
import org.cotrix.domain.version.Version;



/**
 * An {@link Identified}, {@link Attribute}, {@link Named}, and {@link Versioned} set of {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codebag extends Identified,Attributed,Named,Versioned {

	/**
	 * Returns the {@link Codelist}s in this bag.
	 * @return the lists
	 */
	Container<? extends Codelist> lists();

	
	/**
	 * A {@link Versioned.Abstract} implementation of {@link Codebag}.
	 *
	 */
	public class Private extends  Versioned.Abstract<Private> implements  Codebag {
	
		private final Container.Private<Codelist.Private> lists;

		/**
		 * Creates a new instance from a given set of parameters.
		 * @param params the parameters
		 */
		public Private(CodebagPO params) {
			super(params);
			this.lists=params.lists();
		}
		
		@Override
		public Container.Private<Codelist.Private> lists() {
			return lists;
		}
		
		protected void buildPO(IdGenerator generator,CodebagPO po) {
			super.fillPO(generator,po);
			po.setLists(lists().copy(generator));
		}
		
		@Override
		public Codebag.Private copy(IdGenerator generator,Version version) {
			CodebagPO po = new CodebagPO(generator.generateId());
			buildPO(generator,po);
			po.setVersion(version);
			return new Codebag.Private(po);
		}
		
		@Override
		public void update(Private delta) throws IllegalArgumentException, IllegalStateException {
			super.update(delta);
			this.lists().update(delta.lists());
		}

		@Override
		public String toString() {
			return "CodeBag [id="+id()+", name=" + name() + ", lists=" + lists + ", attributes=" + attributes() + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((lists == null) ? 0 : lists.hashCode());
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
			Codebag.Private other = (Codebag.Private) obj;
			if (lists == null) {
				if (other.lists != null)
					return false;
			} else if (!lists.equals(other.lists))
				return false;
			return true;
		}
		
	}
}