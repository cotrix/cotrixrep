package org.cotrix.web.importwizard.client.step.preview;

import org.cotrix.web.importwizard.client.presenter.Presenter;
import org.cotrix.web.importwizard.client.step.Step;
import org.cotrix.web.share.shared.CotrixImportModelController.OnFileChangeHandler;

public interface HeaderSelectionFormPresenter extends Presenter<HeaderSelectionFormPresenterImpl>, HeaderSelectionFormView.Presenter<HeaderSelectionFormPresenterImpl>, Step,OnFileChangeHandler {

}
