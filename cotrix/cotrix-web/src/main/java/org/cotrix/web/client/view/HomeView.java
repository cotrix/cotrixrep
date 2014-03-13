package org.cotrix.web.client.view;

import java.util.List;

import org.cotrix.web.shared.UINews;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(HomeViewImpl.class)
public interface HomeView {

	public void setStatistics(int codelists, int codes, int users, int repositories);

	public Widget asWidget();

	void setNews(List<UINews> newses);
}