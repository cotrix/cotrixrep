/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.Identifiable;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttribute implements Identifiable {
	
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
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodeAttribute [code=");
		builder.append(code);
		builder.append(", attribute=");
		builder.append(attribute);
		builder.append("]");
		return builder.toString();
	}
}
