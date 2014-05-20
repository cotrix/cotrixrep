/**
 * 
 */
package org.cotrix.web.manage.shared.modify;

import org.cotrix.web.common.shared.codelist.Identifiable;

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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AddCommand [item=");
		builder.append(item);
		builder.append("]");
		return builder.toString();
	}
}
