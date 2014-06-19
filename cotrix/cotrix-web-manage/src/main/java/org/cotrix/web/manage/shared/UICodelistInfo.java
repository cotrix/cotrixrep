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
	
	private boolean isUserInTeam;

	public boolean isUserInTeam() {
		return isUserInTeam;
	}

	public void setUserInTeam(boolean isUserInTeam) {
		this.isUserInTeam = isUserInTeam;
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
		builder.append(", isUserInTeam=");
		builder.append(isUserInTeam);
		builder.append("]");
		return builder.toString();
	}

}
