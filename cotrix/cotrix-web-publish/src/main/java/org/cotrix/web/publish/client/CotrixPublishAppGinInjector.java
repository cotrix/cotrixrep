package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.presenter.ChanelPropertyPresenter;
import org.cotrix.web.publish.client.presenter.CodeListPublishPresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(CotrixPublishAppGinModule.class)
public interface CotrixPublishAppGinInjector extends Ginjector {
	public PublishServiceAsync getRpcService();
	public CotrixPublishAppController getAppController();
	public CodeListPublishPresenter getCodeListPublishPresenter();
	public ChanelPropertyPresenter getChanelPropertyPresenter();
}
