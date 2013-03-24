package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.view.form.SummaryFormView;
import org.cotrix.web.share.shared.CotrixImportModelController.OnDescriptionChangeHandler;
import org.cotrix.web.share.shared.CotrixImportModelController.OnFileChangeHandler;
import org.cotrix.web.share.shared.CotrixImportModelController.OnMetaDataChangeHandler;
import org.cotrix.web.share.shared.CotrixImportModelController.OnTypeChangeHandler;

public interface SummaryFormPresenter extends  Presenter<SummaryFormPresenterImpl>,SummaryFormView.Presenter<SummaryFormPresenterImpl> ,OnMetaDataChangeHandler,OnTypeChangeHandler,OnDescriptionChangeHandler,OnFileChangeHandler{

}
