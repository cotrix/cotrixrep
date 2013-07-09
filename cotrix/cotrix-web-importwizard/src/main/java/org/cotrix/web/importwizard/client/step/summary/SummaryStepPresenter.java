package org.cotrix.web.importwizard.client.step.summary;

import org.cotrix.web.importwizard.client.Presenter;
import org.cotrix.web.share.shared.CotrixImportModelController.OnDescriptionChangeHandler;
import org.cotrix.web.share.shared.CotrixImportModelController.OnFileChangeHandler;
import org.cotrix.web.share.shared.CotrixImportModelController.OnMetaDataChangeHandler;
import org.cotrix.web.share.shared.CotrixImportModelController.OnTypeChangeHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface SummaryStepPresenter extends  Presenter<SummaryStepPresenterImpl>, SummaryStepView.Presenter ,OnMetaDataChangeHandler,OnTypeChangeHandler,OnDescriptionChangeHandler,OnFileChangeHandler{

}
