package org.cotrix.web.client;

import org.cotrix.web.client.presenter.CotrixWebPresenter;
import org.cotrix.web.client.presenter.CotrixWebPresenterImpl;
import org.cotrix.web.client.view.CotrixWebView;
import org.cotrix.web.client.view.CotrixWebViewImpl;
import org.cotrix.web.importwizard.client.CotrixImportAppController;
import org.cotrix.web.importwizard.client.CotrixImportAppControllerImpl;
import org.cotrix.web.importwizard.client.step.upload.UploadFormPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.upload.UploadFormView;
import org.cotrix.web.importwizard.client.step.upload.UploadFormViewImpl;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class AppGinModule extends AbstractGinModule {
	@Provides
	@Singleton
	public HandlerManager getEventBus() {
		return new HandlerManager(null);
	}

	@Override
	protected void configure() {
		bind(AppController.class).to(AppControllerImpl.class);

		bind(CotrixWebView.class).to(CotrixWebViewImpl.class);
		bind(CotrixWebPresenter.class).to(CotrixWebPresenterImpl.class);
	}

}
