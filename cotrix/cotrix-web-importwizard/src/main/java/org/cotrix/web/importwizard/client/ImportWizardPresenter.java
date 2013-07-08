package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.step.FormWrapperPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenterImpl.OnUploadFileFinish;

public interface ImportWizardPresenter extends Presenter<GenericImportWizardPresenterImpl>, ImportWizardView.Presenter, FormWrapperPresenter.OnButtonClickHandler,OnUploadFileFinish {
    void addForm(Presenter presenter, String s);
}
