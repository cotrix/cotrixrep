package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.step.done.DoneFormPresenter;
import org.cotrix.web.importwizard.client.step.mapping.HeaderTypeFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.metadata.MetadataFormPresenter;
import org.cotrix.web.importwizard.client.step.preview.HeaderSelectionFormPresenterImpl;
import org.cotrix.web.importwizard.client.step.summary.SummaryFormPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadFormPresenter;
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
    		SummaryFormPresenter summaryFormPresenter,
    		DoneFormPresenter doneFormPresenter) {
    	
        super(rpcService,eventBus,view,model);
        addForm(uploadFormPresenter, "Upload CSV File");
        addForm(metadataFormPresenter, "Add Metadata");
        addForm(headerSelectionFormPresenter, "Select Header");
        addForm(headerTypeFormPresenter, "Define Type");
        addForm(summaryFormPresenter, "Summary");
        addForm(doneFormPresenter, "Done");
    }
}
