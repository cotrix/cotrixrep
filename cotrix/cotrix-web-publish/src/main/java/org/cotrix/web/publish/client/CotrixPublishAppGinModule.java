package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.presenter.ChanelPropertyPresenter;
import org.cotrix.web.publish.client.presenter.ChanelPropertyPresenterImpl;
import org.cotrix.web.publish.client.presenter.CodeListDetailPresenter;
import org.cotrix.web.publish.client.presenter.CodeListDetailPresenterImpl;
import org.cotrix.web.publish.client.presenter.CodeListPresenter;
import org.cotrix.web.publish.client.presenter.CodeListPresenterImpl;
import org.cotrix.web.publish.client.presenter.CodeListPublishPresenter;
import org.cotrix.web.publish.client.presenter.CodeListPublishPresenterImpl;
import org.cotrix.web.publish.client.view.ChanelPropertyView;
import org.cotrix.web.publish.client.view.ChanelPropertyViewImpl;
import org.cotrix.web.publish.client.view.CodeListDetailView;
import org.cotrix.web.publish.client.view.CodeListDetailViewImpl;
import org.cotrix.web.publish.client.view.CodeListPublishView;
import org.cotrix.web.publish.client.view.CodeListPublishViewImpl;
import org.cotrix.web.publish.client.view.CodeListView;
import org.cotrix.web.publish.client.view.CodeListViewImpl;

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
		
		bind(CodeListPublishPresenter.class).to(CodeListPublishPresenterImpl.class);
		bind(CodeListPublishView.class).to(CodeListPublishViewImpl.class);

		bind(CodeListPresenter.class).to(CodeListPresenterImpl.class);
		bind(CodeListView.class).to(CodeListViewImpl.class);
		
		bind(CodeListDetailPresenter.class).to(CodeListDetailPresenterImpl.class);
		bind(CodeListDetailView.class).to(CodeListDetailViewImpl.class);

		bind(ChanelPropertyPresenter.class).to(ChanelPropertyPresenterImpl.class);
		bind(ChanelPropertyView.class).to(ChanelPropertyViewImpl.class);
	}

}
