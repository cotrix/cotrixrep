package org.cotrix.web.importwizard.client.step.mapping;

import org.cotrix.web.importwizard.client.step.WizardStep;
import org.cotrix.web.share.shared.CotrixImportModelController.OnFileChangeHandler;

public interface MappingStepPresenter extends WizardStep, MappingStepFormView.Presenter, OnFileChangeHandler{

}