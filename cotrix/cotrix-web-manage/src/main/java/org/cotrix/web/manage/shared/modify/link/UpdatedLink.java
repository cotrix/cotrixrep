/**
 * 
 */
package org.cotrix.web.manage.shared.modify.link;

import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdatedLink extends ModifyCommandResult {
	
	protected UICode code;
	protected UILink updatedLink;
	
	protected UpdatedLink(){}


	public UpdatedLink(UICode code, UILink link) {
		this.code = code;
		this.updatedLink = link;
	}

	public UICode getCode() {
		return code;
	}

	public UILink getUpdatedLink() {
		return updatedLink;
	}

}
