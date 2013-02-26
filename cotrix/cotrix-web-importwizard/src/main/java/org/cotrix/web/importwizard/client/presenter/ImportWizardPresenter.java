package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.view.ImportWizardView;

public interface ImportWizardPresenter extends Presenter<GenericImportWizardPresenterImpl>, ImportWizardView.Presenter, FormWrapperPresenter.OnButtonClickHandler {
    void addForm(Presenter presenter, String s);
}
