package org.cotrix.web.importwizard.client.util;

import org.cotrix.web.importwizard.client.Presenter;
import org.cotrix.web.importwizard.client.step.Step;
import org.cotrix.web.share.shared.CotrixImportModelController.OnFileChangeHandler;

public interface HeaderDescriptionPresenter extends Presenter<HeaderDescriptionPresenterImpl>,HeaderDescriptionFormView.Presenter<HeaderDescriptionPresenterImpl>,OnFileChangeHandler,Step{

}
