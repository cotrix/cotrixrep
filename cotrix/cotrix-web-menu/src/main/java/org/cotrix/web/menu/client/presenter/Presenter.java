package org.cotrix.web.menu.client.presenter;
import com.google.gwt.user.client.ui.HasWidgets;

public abstract interface Presenter<T> {
  public abstract void go(final HasWidgets container);
}
