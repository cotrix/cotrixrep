package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenterImpl.OnUploadFileFinish;

public interface ImportWizardPresenter extends Presenter, ImportWizardView.Presenter, OnUploadFileFinish {
}
