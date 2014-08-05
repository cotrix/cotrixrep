package org.cotrix.web.manage.client.codelist.common.side;

import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.LoadingPanel;
import org.cotrix.web.manage.client.util.HeaderBuilder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SideTabPanel extends LoadingPanel {

	interface SideTabPanelUiBinder extends UiBinder<Widget, SideTabPanel> {}
	
	private static SideTabPanelUiBinder uiBinder = GWT.create(SideTabPanelUiBinder.class);
	
	@UiField HTML header;
	
	@UiField ScrollPanel contentPanel;
	
	@UiField ItemToolbar toolbar;
	
	@Inject
	private HeaderBuilder headerBuilder;

	@Inject
	protected void init() {
		add(uiBinder.createAndBindUi(this));
	}
	
	protected void setHeader(String type, String subject, String color) {
		header.setHTML(headerBuilder.getHeader(type, subject, color));
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		//workaround issue #7188 https://code.google.com/p/google-web-toolkit/issues/detail?id=7188
		onResize();
	}
}
