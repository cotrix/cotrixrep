package org.cotrix.domain.primitive.link;

import org.cotrix.domain.primitive.entity.Entity;
import org.cotrix.domain.trait.Attributed;

/**
 * An instance of a {@link CodelistLink}.
 * 
 * @author Fabio Simeoni
 *
 */
public interface CodeLink extends Entity<CodeLink>,Attributed {
	
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
}

