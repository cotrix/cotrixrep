/**
 * 
 */
package org.cotrix.web.manage.shared.modify;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class RemoveCommand implements ModifyCommand {
	
	protected String id;
	
	protected RemoveCommand(){}

	/**
	 * @param id
	 */
	public RemoveCommand(String id) {
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
		//TODO replace with getSimpleName() when move to GWT 2.6
		builder.append(getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1));
		builder.append("[id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}
