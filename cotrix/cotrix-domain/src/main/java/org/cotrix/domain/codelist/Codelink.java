package org.cotrix.domain.codelist;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.EntityProvider;
import org.cotrix.domain.trait.Identified;

/**
 * An {@link Identified} and {@link Attributed} instance of a {@link CodelistLink}.
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
	 * @return the target identifier
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
		
			Code.State target = state().target();
			
			//dynamic resolution (includes orphan link case)
			return target== null? null : type().type().value(state().target());
			
		}
		
		
		@Override
		public CodelistLink.Private type() {
			return new CodelistLink.Private(state().type());
		}

		@Override
		public void update(Private changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			Code.State newtarget = changeset.state().target();
			
			if (newtarget!=null)
				state().target(newtarget);
			
			//wont'update type
		}

	}
}
