package org.cotrix.domain;

import org.cotrix.domain.primitive.Container;
import org.cotrix.domain.primitive.PContainer;
import org.cotrix.domain.trait.Attributed;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.trait.Versioned;


/**
 * A collection of {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Codebag extends Identified,Attributed,Named,Versioned {

	/**
	 * Returns the code lists in this bag.
	 * @return the lists.
	 */
	Container<? extends Codelist> lists();

	
	
	/**
	 * A private extension of {@link Codebag}.
	 *
	 */
	public interface Private extends Codebag,Versioned.Private<Private> {
	
		@Override
		public PContainer<Codelist.Private> lists();
	}
}