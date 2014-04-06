package org.cotrix.domain.codelist;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;

/**
 * An {@link Identified} and {@link Attributed} instance of a
 * {@link CodelistLink}.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Codelink extends Identified, Attributed {

	/**
	 * Returns the type of this link.
	 * 
	 * @return the type
	 */
	CodelistLink type();

	/**
	 * Returns the value of this link.
	 * 
	 * @return the link value, or <code>null</code> if the link is orphaned.
	 */
	Object value();

	static interface State extends Identified.State, Attributed.State, EntityProvider<Private> {

		CodelistLink.State type();

		void type(CodelistLink.State state);

		Code.State target();

		void target(Code.State code);

	}

	/**
	 * An {@link Attributed.Abstract} implementation of {@link Codelink}.
	 * 
	 */
	public class Private extends Attributed.Abstract<Private, State> implements Codelink {

		public Private(Codelink.State state) {
			super(state);
		}

		@Override
		public Object value() {

			return resolve(this.state(), this.type().state());

		}

		@Override
		public CodelistLink.Private type() {
			return new CodelistLink.Private(state().type());
		}

		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			Code.State newtarget = changeset.state().target();

			if (newtarget != null)
				state().target(newtarget);
			
			// wont'update type
		}

		
		@Override
		public String toString() {
			return "Codelink [id="+id()+", type=" + state().type() + " :--> " + state().target().id()+"]" ;
		}
		
		
		// extracted to reuse below this layer (for link-of-links) without
		// object instantiation costs
		public static Object resolve(Codelink.State link, CodelistLink.State type) {

			// dynamic resolution (includes orphan link case)
			return link.target() == null ? null : type.valueType().valueIn(link.target());
		}

	}
	
}
