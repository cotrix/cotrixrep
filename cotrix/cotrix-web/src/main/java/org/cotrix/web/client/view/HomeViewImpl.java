package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class HomeViewImpl extends Composite implements HomeView {

	private static HomeUiBinder uiBinder = GWT.create(HomeUiBinder.class);

	@UiTemplate("Home.ui.xml")
	interface HomeUiBinder extends UiBinder<Widget, HomeViewImpl> {
	}

	@UiField DivElement statisticsLoader;
	@UiField HTML statistics;

	public HomeViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setStatistics(int codelists, int codes, int users, int repositories) {
		StringBuilder html = new StringBuilder();
		html.append("<ul>");
		html.append("<li>Codelists: ").append(codelists).append("</li>");
		html.append("<li>Codes: ").append(codes).append("</li>");
		html.append("<li>Users: ").append(users).append("</li>");
		html.append("<li>Repositories: ").append(repositories).append("</li>");
		html.append("</ul>");
		statistics.setHTML(html.toString());
		statisticsLoader.getStyle().setDisplay(Display.NONE);
		statistics.setVisible(true);
	}
}
