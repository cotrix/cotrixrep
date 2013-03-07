package org.cotrix.domain.primitive.link;

import org.cotrix.domain.Codelist;
import org.cotrix.domain.primitive.entity.AttributedEntity;

/**
 * A link between {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface CodelistLink extends AttributedEntity<CodelistLink> {
	
	/**
	 * Returns the identifier of the target codelist.
	 * @return the identifier
	 */
	String targetId();
}
