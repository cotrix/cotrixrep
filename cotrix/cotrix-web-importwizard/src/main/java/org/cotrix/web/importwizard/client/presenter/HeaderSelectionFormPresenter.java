package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.view.form.CotrixForm;
import org.cotrix.web.importwizard.client.view.form.HeaderSelectionFormView;
import org.cotrix.web.share.shared.CotrixImportModelController.OnFileChangeHandler;

public interface HeaderSelectionFormPresenter extends Presenter<HeaderSelectionFormPresenterImpl>, HeaderSelectionFormView.Presenter<HeaderSelectionFormPresenterImpl>, CotrixForm,OnFileChangeHandler {

}
