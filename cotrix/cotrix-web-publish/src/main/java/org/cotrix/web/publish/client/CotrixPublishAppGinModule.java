package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.presenter.CodelistPublishPresenter;
import org.cotrix.web.publish.client.presenter.CodelistPublishPresenterImpl;
import org.cotrix.web.publish.client.view.CodelistPublishView;
import org.cotrix.web.publish.client.view.CodelistPublishViewImpl;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class CotrixPublishAppGinModule extends AbstractGinModule{

    @Provides
    @Singleton
    public HandlerManager getEventBus() {
        return new HandlerManager(null);
    }
    

	@Override
	protected void configure() {
		bind(CotrixPublishAppController.class).to(CotrixPublishAppControllerImpl.class);
		
		bind(CodelistPublishPresenter.class).to(CodelistPublishPresenterImpl.class);
		bind(CodelistPublishView.class).to(CodelistPublishViewImpl.class);
	}

}
