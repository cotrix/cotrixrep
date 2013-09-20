package org.cotrix.web.codelistmanager.client.presenter;

import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.client.event.OpenCodeListEvent;
import org.cotrix.web.codelistmanager.client.view.CodeListView;
import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListPresenterImpl implements CodeListPresenter {

	@Inject
	@ManagerBus
	protected EventBus managerBus;
	
	private CodeListView view;


	@Inject
	public CodeListPresenterImpl(CodeListView view) {
		this.view = view;
		this.view.setPresenter(this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
		view.refresh();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void onCodelistItemSelected(UICodelist codelist) {
		managerBus.fireEvent(new OpenCodeListEvent(codelist));
	}

	public void refresh() {
		view.refresh();
	}

}
