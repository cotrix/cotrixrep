package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.presenter.HeaderDescriptionPresenter;
import org.cotrix.web.importwizard.client.presenter.HeaderDescriptionPresenterImpl;
import org.cotrix.web.importwizard.client.presenter.ImportWizardPresenter;
import org.cotrix.web.importwizard.client.presenter.ImportWizardPresenterImpl;
import org.cotrix.web.importwizard.client.step.done.DoneFormPresenter;
import org.cotrix.web.importwizard.client.step.done.DoneFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.done.DoneFormView;
import org.cotrix.web.importwizard.client.step.done.DoneFormViewImpl;
import org.cotrix.web.importwizard.client.step.mapping.HeaderTypeFormPresenter;
import org.cotrix.web.importwizard.client.step.mapping.HeaderTypeFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.mapping.HeaderTypeFormView;
import org.cotrix.web.importwizard.client.step.mapping.HeaderTypeFormViewImpl;
import org.cotrix.web.importwizard.client.step.metadata.MetadataFormPresenter;
import org.cotrix.web.importwizard.client.step.metadata.MetadataFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.metadata.MetadataFormView;
import org.cotrix.web.importwizard.client.step.metadata.MetadataFormViewImpl;
import org.cotrix.web.importwizard.client.step.preview.HeaderSelectionFormPresenter;
import org.cotrix.web.importwizard.client.step.preview.HeaderSelectionFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.preview.HeaderSelectionFormView;
import org.cotrix.web.importwizard.client.step.preview.HeaderSelectionFormViewImpl;
import org.cotrix.web.importwizard.client.step.summary.SummaryFormPresenter;
import org.cotrix.web.importwizard.client.step.summary.SummaryFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.summary.SummaryFormView;
import org.cotrix.web.importwizard.client.step.summary.SummaryFormViewImpl;
import org.cotrix.web.importwizard.client.step.upload.UploadFormPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.upload.UploadFormView;
import org.cotrix.web.importwizard.client.step.upload.UploadFormViewImpl;
import org.cotrix.web.importwizard.client.view.ImportWizardView;
import org.cotrix.web.importwizard.client.view.ImportWizardViewImpl;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormView;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormViewImpl;
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
      
        bind(DoneFormView.class).to(DoneFormViewImpl.class);
        bind(DoneFormPresenter.class).to(DoneFormPresenterImpl.class);
    }
}
