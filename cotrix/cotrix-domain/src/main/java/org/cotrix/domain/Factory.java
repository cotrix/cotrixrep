package org.cotrix.domain;

import org.cotrix.domain.pos.AttributePO;
import org.cotrix.domain.pos.CodePO;
import org.cotrix.domain.pos.CodebagPO;
import org.cotrix.domain.pos.CodelistPO;
import org.cotrix.domain.utils.IdGenerator;

/**
 * Return implementation of domain object.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Factory extends IdGenerator {

	/**
	 * Returns an {@link Attribute} with given parameters.
	 * @param params the parameters
	 * @return the attribute
	 */
	Attribute attribute(AttributePO params);
	
	/**
	 * Returns a {@link Code} with given parameters.
	 * @param params the parameters
	 * @return the code
	 */
	Code code(CodePO params);
	
	/**
	 * Returns a {@link Codelist} with given parameters.
	 * @param params the parameters
	 * @return the codelist
	 */
	Codelist codelist(CodelistPO params) throws IllegalArgumentException;
	
	
	/**
	 * Returns a {@link Codebag} with given parameters.
	 * @param params the parameters
	 * @return the codebag
	 */
	Codebag codebag(CodebagPO bag) throws IllegalArgumentException;
	
	
}
