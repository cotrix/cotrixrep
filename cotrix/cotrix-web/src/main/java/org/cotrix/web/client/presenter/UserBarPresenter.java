package org.cotrix.web.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(UserBarPresenterImpl.class)
public interface UserBarPresenter extends org.cotrix.web.client.view.UserBarView.Presenter {
	
	 public void go(final HasWidgets container);

}