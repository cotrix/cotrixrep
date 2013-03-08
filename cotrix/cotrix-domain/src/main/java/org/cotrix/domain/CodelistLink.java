package org.cotrix.domain;

import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;

/**
 * A link between {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface CodelistLink extends Identified, Attributed,Named {
	
	/**
	 * Returns the identifier of the target codelist.
	 * @return the identifier
	 */
	String targetId();
	
	
	/**
	 * A private extension of {@link CodelistLink}.
	 * 
	 */
	public interface Private extends CodelistLink, Named.Private<Private> {}
}
