package org.cotrix.web.importwizard.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.cotrix.web.importwizard.client.presenter.*;
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
import org.cotrix.web.importwizard.shared.CotrixImportModel;

public class AppGinModule extends AbstractGinModule {

    @Provides
    @Singleton
    public HandlerManager getEventBus() {
        return new HandlerManager(null);
    }
   
    @Provides
    @Singleton
    public CotrixImportModel getModel() {
    	return new CotrixImportModel();
    }

    protected void configure() {
        bind(AppController.class).to(AppControllerImpl.class);
      
        bind(UploadFormView.class).to(UploadFormViewImpl.class);
        bind(UploadFormPresenter.class).to(UploadFormPresenterImpl.class);
       
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
