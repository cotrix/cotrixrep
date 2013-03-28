package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.view.ImportWizardView;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.inject.Inject;

public class ImportWizardPresenterImpl extends GenericImportWizardPresenterImpl {

    @Inject
    public ImportWizardPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, ImportWizardView view,CotrixImportModelController model, 
    		UploadFormPresenter uploadFormPresenter,
    		MetadataFormPresenter metadataFormPresenter,
    		HeaderSelectionFormPresenterImpl headerSelectionFormPresenter,
    		HeaderDescriptionPresenterImpl headerDescriptionPresenter,
    		HeaderTypeFormPresenterImpl headerTypeFormPresenter,
    		SummaryFormPresenter summaryFormPresenter) {
    	
        super(rpcService,eventBus,view,model);
        addForm(uploadFormPresenter, "Upload CSV File");
        addForm(metadataFormPresenter, "Add Metadata");
        addForm(headerSelectionFormPresenter, "Select Header");
//        addForm(headerDescriptionPresenter, "Describe Header");
        addForm(headerTypeFormPresenter, "Define Type");
        addForm(summaryFormPresenter, "Summary");
    }
}
