package org.cotrix.domain.pos;

import org.cotrix.domain.Code;


/**
 * A set of parameters required to create a {@link Code}.
 * 
 * @author Fabio Simeoni
 *
 */
public final class CodePO extends AttributedPO {

	/**
	 * Creates an instance with an identifier.
	 * @param id the identifier
	 */
	public CodePO(String id) {
		super(id);
	}
}
