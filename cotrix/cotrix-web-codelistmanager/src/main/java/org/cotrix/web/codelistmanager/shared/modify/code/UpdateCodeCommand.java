/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify.code;

import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.share.shared.codelist.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdateCodeCommand implements ModifyCommand, CodeCommand  {
	
	protected String codeId;
	protected UIQName name;
	
	protected UpdateCodeCommand(){}

	/**
	 * @param name
	 */
	public UpdateCodeCommand(String codeId, UIQName name) {
		this.codeId = codeId;
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public UIQName getName() {
		return name;
	}

	/**
	 * @return the codeId
	 */
	public String getCodeId() {
		return codeId;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateCodeCommand [codeId=");
		builder.append(codeId);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}
}
