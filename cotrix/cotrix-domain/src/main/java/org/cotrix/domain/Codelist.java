package org.cotrix.domain;

import org.cotrix.domain.primitive.container.Container;
import org.cotrix.domain.primitive.entity.VersionedEntity;
import org.cotrix.domain.primitive.link.CodelistLink;


/**
 * A list of {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codelist extends VersionedEntity<Codelist> {

	/**
	 * Returns the codes of this list.
	 * @return the codes
	 */
	Container<Code> codes();
	
	/**
	 * Returns the links of this list.
	 * @return the links.
	 */
	Container<CodelistLink> links();

}