/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify;

import org.cotrix.web.codelistmanager.shared.Identifiable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AddCommand<T extends Identifiable> implements ModifyCommand, ContainsIdentifiable {
	
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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Identifiable getIdentifiable() {
		return item;
	}
	
}
