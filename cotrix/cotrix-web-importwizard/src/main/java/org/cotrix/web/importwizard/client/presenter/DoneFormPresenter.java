package org.cotrix.web.importwizard.client.presenter;

import org.cotrix.web.importwizard.client.view.form.CotrixForm;
import org.cotrix.web.importwizard.client.view.form.DoneFormView;

public interface DoneFormPresenter extends Presenter<DoneFormPresenterImpl>, DoneFormView.Presenter, CotrixForm{
	public void setDoneTitle(String title);
	public void setWarningMessage(String message);
}
