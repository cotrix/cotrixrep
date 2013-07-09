package org.cotrix.web.importwizard.client.step.preview;

import org.cotrix.web.importwizard.client.Presenter;
import org.cotrix.web.importwizard.client.step.WizardStep;
import org.cotrix.web.share.shared.CotrixImportModelController.OnFileChangeHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PreviewStepPresenter extends Presenter<PreviewStepPresenterImpl>, PreviewStepView.Presenter, WizardStep,OnFileChangeHandler {

}
