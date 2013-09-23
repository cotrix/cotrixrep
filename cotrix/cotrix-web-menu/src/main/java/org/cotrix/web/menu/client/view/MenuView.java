package org.cotrix.web.menu.client.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface MenuView {
	
	public enum Menu {
		HOME, 
		IMPORT, 
		MANAGE, 
		PUBLISH;
	};

	public interface Presenter {
		void onMenuClicked(Menu menu);
	}

	void setPresenter(Presenter presenter);
	Widget asWidget();
}
