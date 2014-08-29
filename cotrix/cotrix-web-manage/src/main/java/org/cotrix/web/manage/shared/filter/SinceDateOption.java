/**
 * 
 */
package org.cotrix.web.manage.shared.filter;

import java.util.Date;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SinceDateOption implements FilterOption {
	
	private Date since;
	
	public SinceDateOption(){}

	public SinceDateOption(Date since) {
		this.since = since;
	}

	public Date getSince() {
		return since;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SinceDateOption [since=");
		builder.append(since);
		builder.append("]");
		return builder.toString();
	}
}
