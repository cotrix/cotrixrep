/**
 * 
 */
package org.cotrix.web.manage.client.data;

import java.util.List;

import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.HasValue;
import org.cotrix.web.common.shared.codelist.Identifiable;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.shared.modify.ContainsValued;
import org.cotrix.web.manage.shared.modify.HasCode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeLink implements HasCode, Identifiable, ContainsValued, HasAttributes {
	
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
	public List<UIAttribute> getAttributes() {
		return link.getAttributes();
	}
	
	@Override
	public void setAttributes(List<UIAttribute> attributes) {
		link.setAttributes(attributes);
	}
	
	@Override
	public HasValue getValued() {
		return link;
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
