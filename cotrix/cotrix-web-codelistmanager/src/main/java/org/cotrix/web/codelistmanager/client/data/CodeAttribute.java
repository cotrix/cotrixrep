/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.UICode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttribute {
	
	protected UICode code;
	protected UIAttribute attribute;
	
	/**
	 * @param codeId
	 * @param attribute
	 */
	public CodeAttribute(UICode code, UIAttribute attribute) {
		this.code = code;
		this.attribute = attribute;
	}
	/**
	 * @return the codeId
	 */
	public UICode getCode() {
		return code;
	}
	/**
	 * @return the attribute
	 */
	public UIAttribute getAttribute() {
		return attribute;
	}
}
