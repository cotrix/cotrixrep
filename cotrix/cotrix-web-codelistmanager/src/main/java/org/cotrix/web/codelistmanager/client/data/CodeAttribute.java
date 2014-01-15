/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.shared.modify.HasCode;
import org.cotrix.web.share.shared.codelist.Identifiable;
import org.cotrix.web.share.shared.codelist.UIAttribute;
import org.cotrix.web.share.shared.codelist.UICode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttribute implements HasCode, Identifiable {
	
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
	
	@Override
	public String getId() {
		return attribute.getId();
	}
	
	@Override
	public void setId(String id) {
		attribute.setId(id);
		
	}
}
