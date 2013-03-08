package org.cotrix.domain;

import org.cotrix.domain.primitive.Container;
import org.cotrix.domain.primitive.PContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Versioned;


/**
 * A list of {@link Code}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codelist extends Identified,Attributed,Named,Versioned {

	/**
	 * Returns the codes of this list.
	 * @return the codes
	 */
	Container<? extends Code> codes();
	
	/**
	 * Returns the links of this list.
	 * @return the links.
	 */
	Container<? extends CodelistLink> links();

	
	public interface Private extends Codelist,Versioned.Private<Private> {
		
		@Override
		public PContainer<Code.Private> codes();
	}
}