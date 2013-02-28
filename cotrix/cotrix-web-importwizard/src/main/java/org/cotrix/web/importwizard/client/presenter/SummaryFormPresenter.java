package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.view.form.SummaryFormView;
import org.cotrix.web.importwizard.shared.CSVFile.OnFileChangeHandler;
import org.cotrix.web.importwizard.shared.CotrixImportModel.OnDescriptionChangeHandler;
import org.cotrix.web.importwizard.shared.CotrixImportModel.OnMetaDataChangeHandler;
import org.cotrix.web.importwizard.shared.CotrixImportModel.OnTypeChangeHandler;

public interface SummaryFormPresenter extends  Presenter<SummaryFormPresenterImpl>,SummaryFormView.Presenter<SummaryFormPresenterImpl> ,OnMetaDataChangeHandler,OnTypeChangeHandler,OnDescriptionChangeHandler,OnFileChangeHandler{

}
