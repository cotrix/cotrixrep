package org.cotrix.domain;

import org.cotrix.domain.common.BaseGroup;
import org.cotrix.domain.common.VersionedObject;


/**
 * A list of {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codelist extends VersionedObject<Codelist> {

	/**
	 * Returns the codes of this list.
	 * @return the codes
	 */
	BaseGroup<Code> codes();

}