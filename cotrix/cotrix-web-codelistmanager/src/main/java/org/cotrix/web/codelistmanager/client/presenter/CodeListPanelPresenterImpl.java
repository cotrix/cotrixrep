package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.view.CodeListPanelView;
import org.cotrix.web.codelistmanager.client.view.CodeListRowDataProvider;
import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListPanelPresenterImpl implements CodeListPanelPresenter {

	protected CodeListPanelView view;

	@Inject
	public CodeListPanelPresenterImpl(CodeListPanelView view) {
		this.view = view;
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	@Override
	public CodeListPanelView getView() {
		return view;
	}
	
	@Override
	public void setCodeList(UICodelist codelist)
	{
		CodeListRowDataProvider provider = new CodeListRowDataProvider(codelist.getId());
		view.setProvider(provider);
		
	}

}
