package org.cotrix.domain;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;


/**
 * An instance of a {@link CodelistLink}.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codelink extends Identified,Attributed {
	
	/**
	 * Returns the definition of this link. 
	 * @return the definition
	 */
	CodelistLink definition();

	/**
	 * Returns the identifier of the target of this link.
	 * @return the target identifier
	 */
	String targetId();
	
	/**
	 * A private extension of {@link Codelink}.
	 * 
	 */
	public interface Private extends Codelink, Attributed.Private<Private> {
		
		@Override
		public CodelistLink.Private definition();
	
	}
}

