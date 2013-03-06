package org.cotrix.domain.primitive.link;

import org.cotrix.domain.primitive.entity.AttributedEntity;

/**
 * An instance of a {@link CodelistLink}.
 * 
 * @author Fabio Simeoni
 *
 */
public interface CodeLink extends AttributedEntity<CodeLink> {
	
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

