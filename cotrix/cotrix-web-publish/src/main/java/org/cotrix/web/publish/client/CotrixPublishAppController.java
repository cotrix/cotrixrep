package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.presenter.Presenter;

import com.google.gwt.event.logical.shared.ValueChangeHandler;

public interface CotrixPublishAppController extends Presenter, ValueChangeHandler<String> {
	public void refresh();
}
