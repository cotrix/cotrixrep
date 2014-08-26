/**
 * 
 */
package org.cotrix.web.manage.shared;

import java.util.Date;

import org.cotrix.web.common.shared.codelist.UICodelist;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UICodelistInfo extends UICodelist {
	
	private boolean isUserInTeam;
	private Date creationDate;

	public boolean isUserInTeam() {
		return isUserInTeam;
	}

	public void setUserInTeam(boolean isUserInTeam) {
		this.isUserInTeam = isUserInTeam;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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
		builder.append(", creationDate=");
		builder.append(creationDate);
		builder.append("]");
		return builder.toString();
	}

}
