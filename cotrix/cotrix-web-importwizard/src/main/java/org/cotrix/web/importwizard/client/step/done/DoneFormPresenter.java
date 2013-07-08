package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.presenter.Presenter;
import org.cotrix.web.importwizard.client.step.Step;

public interface DoneFormPresenter extends Presenter<DoneFormPresenterImpl>, DoneFormView.Presenter, Step{
	public void setDoneTitle(String title);
	public void setWarningMessage(String message);
}
