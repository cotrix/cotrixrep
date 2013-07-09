package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.step.done.DoneStepPresenter;
import org.cotrix.web.importwizard.client.step.mapping.MappingStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.metadata.MetadataStepPresenter;
import org.cotrix.web.importwizard.client.step.preview.PreviewStepPresenterImpl;
import org.cotrix.web.importwizard.client.step.summary.SummaryStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenter;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.inject.Inject;

public class ImportWizardPresenterImpl extends GenericImportWizardPresenterImpl {

    @Inject
    public ImportWizardPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, ImportWizardView view,CotrixImportModelController model, 
    		UploadStepPresenter uploadFormPresenter,
    		MetadataStepPresenter metadataFormPresenter,
    		PreviewStepPresenterImpl headerSelectionFormPresenter,
    		MappingStepPresenterImpl headerTypeFormPresenter,
    		SummaryStepPresenter summaryFormPresenter,
    		DoneStepPresenter doneFormPresenter) {
    	
        super(rpcService,eventBus,view,model);
        addStep(uploadFormPresenter, "Upload CSV File");
        addStep(metadataFormPresenter, "Add Metadata");
        addStep(headerSelectionFormPresenter, "Select Header");
        addStep(headerTypeFormPresenter, "Define Type");
        addStep(summaryFormPresenter, "Summary");
        addStep(doneFormPresenter, "Done");
    }
}
