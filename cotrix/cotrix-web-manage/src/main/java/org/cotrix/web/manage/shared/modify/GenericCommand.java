/**
 * 
 */
package org.cotrix.web.manage.shared.modify;

import org.cotrix.web.common.shared.codelist.Identifiable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class GenericCommand<T extends Identifiable> implements ModifyCommand, ContainsIdentifiable {
	
	public enum Action {ADD, UPDATE, REMOVE};
	
	private Action action;
	
	private T item;
	
	public GenericCommand(){}

	/**
	 * @param item
	 */
	public GenericCommand(Action action, T item) {
		this.action = action;
		this.item = item;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
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
		//TODO replace with getSimpleName() when move to GWT 2.6
		builder.append(getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1));
		builder.append(" [action=");
		builder.append(action);
		builder.append(", item=");
		builder.append(item);
		builder.append("]");
		return builder.toString();
	}
}
