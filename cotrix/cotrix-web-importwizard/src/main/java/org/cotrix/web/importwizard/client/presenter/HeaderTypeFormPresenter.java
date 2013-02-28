package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.view.form.CotrixForm;
import org.cotrix.web.importwizard.client.view.form.HeaderTypeFormView;
import org.cotrix.web.importwizard.shared.CSVFile.OnFileChangeHandler;

public interface HeaderTypeFormPresenter extends Presenter<HeaderTypeFormPresenterImpl>,HeaderTypeFormView.Presenter<HeaderTypeFormPresenterImpl>,CotrixForm,OnFileChangeHandler{

}