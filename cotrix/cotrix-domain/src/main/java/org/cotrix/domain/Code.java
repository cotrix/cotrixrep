package org.cotrix.domain;

import org.cotrix.domain.primitive.Container;
import org.cotrix.domain.primitive.PContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;



/**
 * A code.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Code extends Identified, Attributed,Named {
	
	Container<? extends Codelink> links();
	
	/**
	 * A private extension of {@link Code}
	 */
	public interface Private extends Code, Named.Private<Private> {
		
		PContainer<Codelink.Private> links();
	}
}