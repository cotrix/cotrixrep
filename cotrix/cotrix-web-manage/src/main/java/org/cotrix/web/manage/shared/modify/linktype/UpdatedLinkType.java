/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linktype;

import java.util.List;

import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.shared.modify.HasId;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdatedLinkType extends ModifyCommandResult implements HasId, HasAttributes {
	
	protected UILinkType updatedLinkType;
	
	protected UpdatedLinkType(){}

	public UpdatedLinkType(UILinkType link) {
		this.updatedLinkType = link;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return updatedLinkType.getId();
	}
	
	@Override
	public List<UIAttribute> getAttributes() {
		return updatedLinkType.getAttributes();
	}
	
	@Override
	public void setAttributes(List<UIAttribute> attributes) {
		updatedLinkType.setAttributes(attributes);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdatedLinkType [updatedLinkType=");
		builder.append(updatedLinkType);
		builder.append("]");
		return builder.toString();
	}
}
