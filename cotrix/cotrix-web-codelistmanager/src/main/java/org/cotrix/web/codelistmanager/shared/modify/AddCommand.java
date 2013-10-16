/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AddCommand<T> implements ModifyCommand {
	
	protected T item;
	
	protected AddCommand(){}

	/**
	 * @param item
	 */
	public AddCommand(T item) {
		this.item = item;
	}

	/**
	 * @return the item
	 */
	public T getItem() {
		return item;
	}
}
