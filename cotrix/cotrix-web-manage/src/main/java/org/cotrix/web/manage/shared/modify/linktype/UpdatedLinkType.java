/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linktype;

import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.shared.modify.ContainsAttributed;
import org.cotrix.web.manage.shared.modify.HasId;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdatedLinkType extends ModifyCommandResult implements ContainsAttributed, HasId {
	
	protected UILinkType link;
	
	protected UpdatedLinkType(){}

	/**
	 * @param id
	 */
	public UpdatedLinkType(UILinkType link) {
		this.link = link;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return link.getId();
	}

	@Override
	public HasAttributes getAttributed() {
		return link;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdatedLinkType [link=");
		builder.append(link);
		builder.append("]");
		return builder.toString();
	}
}
