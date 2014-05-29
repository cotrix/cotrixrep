/**
 * 
 */
package org.cotrix.web.manage.shared.modify;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class UpdateCommand<T> implements ModifyCommand {
	
	protected T item;
	
	protected UpdateCommand(){}

	/**
	 * @param item
	 */
	public UpdateCommand(T item) {
		this.item = item;
	}

	/**
	 * @return the item
	 */
	public T getItem() {
		return item;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		//TODO replace with getSimpleName() when move to GWT 2.6
		builder.append(getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1));
		builder.append(" [item=");
		builder.append(item);
		builder.append("]");
		return builder.toString();
	}
}
