package org.cotrix.web.publish.client.presenter;

import org.cotrix.web.publish.client.view.CodeListDetailView;

public interface CodeListDetailPresenter extends Presenter<CodeListDetailPresenter>,CodeListDetailView.Presenter<CodeListDetailPresenter> {
	public void setData(int id);
}
