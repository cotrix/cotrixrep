package org.cotrix.web.publish.client.presenter;

import org.cotrix.web.publish.client.view.ChanelPropertyDialogView;
import org.cotrix.web.publish.shared.ChanelPropertyModelController;

public interface ChanelPropertyDialogPresenter extends Presenter<ChanelPropertyDialogPresenter>, ChanelPropertyDialogView.Presenter<ChanelPropertyDialogPresenter>{
	public void show();
	public void setModel(ChanelPropertyModelController model);
}
