/**
 * 
 */
package org.cotrix.web.common.client.widgets.group;

import com.google.gwt.view.client.ListDataProvider;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Group<T> {
	
	private String name;
	
	private ListDataProvider<T> items;
	
	public Group(String name) {
		this.name = name;
		items = new ListDataProvider<T>();
	}
	
	public String getName() {
		return name;
	}

	public void addItem(T item) {
		items.getList().add(item);
	}

	public ListDataProvider<T> getItems() {
		return items;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group<?> other = (Group<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
