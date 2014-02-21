/**
 * 
 */
package org.cotrix.web.manage.shared.modify.metadata;

import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.manage.shared.modify.ModifyCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdateMetadataCommand implements ModifyCommand, MetadataCommand {
	
	protected UIQName name;
	
	protected UpdateMetadataCommand(){}

	/**
	 * @param name
	 */
	public UpdateMetadataCommand(UIQName name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public UIQName getName() {
		return name;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateMetadataCommand [name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}
}
