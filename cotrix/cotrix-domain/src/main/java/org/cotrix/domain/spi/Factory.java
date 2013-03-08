package org.cotrix.domain.spi;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.Codelink.Private;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.po.AttributePO;
import org.cotrix.domain.po.CodeLinkPO;
import org.cotrix.domain.po.CodePO;
import org.cotrix.domain.po.CodebagPO;
import org.cotrix.domain.po.CodelistLinkPO;
import org.cotrix.domain.po.CodelistPO;

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
	Attribute.Private attribute(AttributePO params);
	
	/**
	 * Returns a {@link Code} with given parameters.
	 * @param params the parameters
	 * @return the code
	 */
	Code.Private code(CodePO params);
	
	/**
	 * Returns a {@link Codelist} with given parameters.
	 * @param params the parameters
	 * @return the codelist
	 */
	Codelist.Private codelist(CodelistPO params);
	
	
	/**
	 * Returns a {@link Codebag} with given parameters.
	 * @param params the parameters
	 * @return the codebag
	 */
	Codebag.Private codebag(CodebagPO params);
	
	/**
	 * Returns a {@link CodelistLink} with given parameters
	 * @param params the parameters
	 * @return the link
	 */
	CodelistLink.Private codelistLink(CodelistLinkPO params);
	
	
	/**
	 * Returns a {@link Codelink} with given parameters
	 * @param params the parameters
	 * @return the link
	 */
	Private codeLink(CodeLinkPO params);
	
}
