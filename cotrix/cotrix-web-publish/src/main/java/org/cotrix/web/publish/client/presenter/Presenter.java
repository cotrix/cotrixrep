package org.cotrix.web.publish.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

public abstract interface Presenter<T> {
	public abstract void go(final HasWidgets container);
}
