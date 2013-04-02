package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.presenter.HeaderDescriptionPresenter;
import org.cotrix.web.importwizard.client.presenter.HeaderDescriptionPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.HeaderSelectionFormPresenter;
import org.cotrix.web.importwizard.client.presenter.HeaderSelectionFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.HeaderTypeFormPresenter;
import org.cotrix.web.importwizard.client.presenter.HeaderTypeFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.ImportWizardPresenter;
import org.cotrix.web.importwizard.client.presenter.ImportWizardPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.MetadataFormPresenter;
import org.cotrix.web.importwizard.client.presenter.MetadataFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenter;
import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.UploadFormPresenter;
import org.cotrix.web.importwizard.client.presenter.UploadFormPresenterImpl;
import org.cotrix.web.importwizard.client.view.ImportWizardView;
import org.cotrix.web.importwizard.client.view.ImportWizardViewImpl;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.HeaderTypeFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderTypeFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.MetadataFormView;
import org.cotrix.web.importwizard.client.view.form.MetadataFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.SummaryFormView;
import org.cotrix.web.importwizard.client.view.form.SummaryFormViewImpl;
import org.cotrix.web.importwizard.client.view.form.UploadFormView;
import org.cotrix.web.importwizard.client.view.form.UploadFormViewImpl;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class CotrixImportAppGinModule extends AbstractGinModule {

    @Provides
    @Singleton
    public HandlerManager getEventBus() {
        return new HandlerManager(null);
    }
   
    @Provides
    @Singleton
    public CotrixImportModelController getModel() {
    	return new CotrixImportModelController();
    }

    protected void configure() {
        bind(CotrixImportAppController.class).to(CotrixImportAppControllerImpl.class);
      
        bind(UploadFormView.class).to(UploadFormViewImpl.class).asEagerSingleton();
        bind(UploadFormPresenter.class).to(UploadFormPresenterImpl.class).asEagerSingleton();
       
        bind(ImportWizardView.class).to(ImportWizardViewImpl.class).in(Singleton.class);
        bind(ImportWizardPresenter.class).to(ImportWizardPresenterImpl.class);
        
        bind(MetadataFormView.class).to(MetadataFormViewImpl.class);
        bind(MetadataFormPresenter.class).to(MetadataFormPresenterImpl.class);
        
        bind(HeaderSelectionFormView.class).to(HeaderSelectionFormViewImpl.class);
        bind(HeaderSelectionFormPresenter.class).to(HeaderSelectionFormPresenterImpl.class);
        
        bind(HeaderDescriptionFormView.class).to(HeaderDescriptionFormViewImpl.class);
        bind(HeaderDescriptionPresenter.class).to(HeaderDescriptionPresenterImpl.class);
        
        bind(HeaderTypeFormView.class).to(HeaderTypeFormViewImpl.class);
        bind(HeaderTypeFormPresenter.class).to(HeaderTypeFormPresenterImpl.class);
        
        bind(SummaryFormView.class).to(SummaryFormViewImpl.class);
        bind(SummaryFormPresenter.class).to(SummaryFormPresenterImpl.class);
    }
}
