package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.step.WizardStepWrapperPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenterImpl.OnUploadFileFinish;

public interface ImportWizardPresenter extends Presenter<GenericImportWizardPresenterImpl>, ImportWizardView.Presenter, WizardStepWrapperPresenter.OnButtonClickHandler,OnUploadFileFinish {
    void addForm(Presenter presenter, String s);
}
