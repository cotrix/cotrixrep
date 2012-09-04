package org.cotrix.web.web.client.common;

public abstract class ColumnDefinition<T> {
	public abstract void render(T t, StringBuilder sb);

	public boolean isClickable() {
		return false;
	}

	public boolean isSelectable() {
		return false;
	}
}
