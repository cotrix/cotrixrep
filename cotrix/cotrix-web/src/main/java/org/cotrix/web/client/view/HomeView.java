package org.cotrix.web.client.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface HomeView {

	public void setStatistics(int codelists, int codes, int users,
			int repositories);

	public Widget asWidget();
}