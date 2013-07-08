package org.cotrix.web.importwizard.client.step.mapping;

import org.cotrix.web.importwizard.client.presenter.Presenter;
import org.cotrix.web.importwizard.client.step.Step;
import org.cotrix.web.share.shared.CotrixImportModelController.OnFileChangeHandler;

public interface HeaderTypeFormPresenter extends Presenter<HeaderTypeFormPresenterImpl>,HeaderTypeFormView.Presenter<HeaderTypeFormPresenterImpl>,Step,OnFileChangeHandler{

}