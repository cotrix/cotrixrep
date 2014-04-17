/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.HasValue;
import org.cotrix.web.common.shared.codelist.Identifiable;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.shared.modify.HasCode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeLink implements HasCode, Identifiable, HasValue {
	
	protected UICode code;
	protected UILink link;
	
	/**
	 * @param codeId
	 * @param attribute
	 */
	public CodeLink(UICode code, UILink link) {
		this.code = code;
		this.link = link;
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
	public UILink getLink() {
		return link;
	}
	
	@Override
	public String getId() {
		return link.getId();
	}
	
	@Override
	public void setId(String id) {
		link.setId(id);
	}
	@Override
	public String getValue() {
		return link.getValue();
	}
	@Override
	public void setValue(String value) {
		link.setValue(value);
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodeLink [code=");
		builder.append(code);
		builder.append(", link=");
		builder.append(link);
		builder.append("]");
		return builder.toString();
	}
}
