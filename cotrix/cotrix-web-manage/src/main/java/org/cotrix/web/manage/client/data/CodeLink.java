/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeLink {
	
	protected UICode code;
	protected UILink link;
	

	public CodeLink(UICode code, UILink link) {
		this.code = code;
		this.link = link;
	}

	public UICode getCode() {
		return code;
	}

	public UILink getLink() {
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
