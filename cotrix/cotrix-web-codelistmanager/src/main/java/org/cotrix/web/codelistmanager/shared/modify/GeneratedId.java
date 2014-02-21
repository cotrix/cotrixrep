/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GeneratedId extends ModifyCommandResult implements HasId {
	
	protected String id;
	
	protected GeneratedId(){}

	/**
	 * @param id
	 */
	public GeneratedId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GeneratedId [id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}
