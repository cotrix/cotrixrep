/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify;

import org.cotrix.web.share.shared.codelist.UICode;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdatedCode extends ModifyCommandResult implements HasCode, HasId {
	
	protected String id;
	protected UICode code;
	
	protected UpdatedCode(){}

	/**
	 * @param id
	 */
	public UpdatedCode(String id, UICode code) {
		this.id = id;
		this.code = code;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	@Override
	public UICode getCode() {
		return code;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdatedCode [id=");
		builder.append(id);
		builder.append(", code=");
		builder.append(code);
		builder.append("]");
		return builder.toString();
	}
}
