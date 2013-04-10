package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.presenter.CodelistPublishPresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(CotrixPublishAppGinModule.class)
public interface CotrixPublishAppGinInjector extends Ginjector {
	public PublishServiceAsync getRpcService();
	public CotrixPublishAppController getAppController();
	public CodelistPublishPresenter getCodeListPublishPresenter();
}
