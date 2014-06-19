/**
 * 
 */
package org.cotrix.web.manage.shared;

import org.cotrix.web.common.shared.codelist.UICodelist;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UICodelistInfo extends UICodelist {
	
	private boolean isOwner;

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UICodelistInfo [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", version=");
		builder.append(version);
		builder.append(", state=");
		builder.append(state);
		builder.append(", isOwner=");
		builder.append(isOwner);
		builder.append("]");
		return builder.toString();
	}

}
