/**
 * 
 */
package org.cotrix.web.manage.shared.modify.link;

import java.util.Collections;
import java.util.List;

import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.HasValue;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.shared.modify.HasCode;
import org.cotrix.web.manage.shared.modify.HasId;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdatedLink extends ModifyCommandResult implements HasCode, HasId, HasValue, HasAttributes {
	
	protected UICode code;
	protected UILink link;
	
	protected UpdatedLink(){}

	/**
	 * @param id
	 */
	public UpdatedLink(UICode code, UILink link) {
		this.code = code;
		this.link = link;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return link!=null?link.getId():null;
	}

	@Override
	public UICode getCode() {
		return code;
	}

	@Override
	public String getValue() {
		return link!=null?link.getValue():null;
	}

	@Override
	public void setValue(String value) {		
	}

	@Override
	public List<UIAttribute> getAttributes() {
		return link!=null?link.getAttributes():Collections.<UIAttribute>emptyList();
	}

	@Override
	public void setAttributes(List<UIAttribute> attributes) {
	}
}
