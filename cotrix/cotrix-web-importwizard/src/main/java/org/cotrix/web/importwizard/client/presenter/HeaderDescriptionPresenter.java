package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.view.form.CotrixForm;
import org.cotrix.web.importwizard.client.view.form.HeaderDescriptionFormView;
import org.cotrix.web.importwizard.shared.CSVFile.OnFileChangeHandler;

public interface HeaderDescriptionPresenter extends Presenter<HeaderDescriptionPresenterImpl>,HeaderDescriptionFormView.Presenter<HeaderDescriptionPresenterImpl>,OnFileChangeHandler,CotrixForm{

}
