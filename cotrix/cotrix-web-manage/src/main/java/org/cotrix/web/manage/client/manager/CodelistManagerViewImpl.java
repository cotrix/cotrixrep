package org.cotrix.web.manage.client.manager;


import org.cotrix.web.common.client.resources.CommonResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistManagerViewImpl extends ResizeComposite implements CodelistManagerView {

	@UiTemplate("CodelistManager.ui.xml")
	interface CodeListManagerUiBinder extends UiBinder<Widget, CodelistManagerViewImpl> {}
	private static CodeListManagerUiBinder uiBinder = GWT.create(CodeListManagerUiBinder.class);

	@UiField SplitLayoutPanel mainPanel;
	@UiField SimpleLayoutPanel westPanel;
	@UiField(provided=true) ContentPanel contentPanel;

	@Inject
	public CodelistManagerViewImpl(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;
		initWidget(uiBinder.createAndBindUi(this));
		CommonResources.INSTANCE.css().ensureInjected();
		mainPanel.setWidgetToggleDisplayAllowed(westPanel, true);
	}

	public HasWidgets getWestPanel() {
		return westPanel;
	}
	
	public ContentPanel getContentPanel() {
		return contentPanel;
	}

	public void showWestPanel(boolean show) {
		mainPanel.setWidgetHidden(westPanel, !show);
	}
	
	@Override
	public void showAlert(String message)
	{
	    final DecoratedPopupPanel simplePopup = new DecoratedPopupPanel(true);
	    simplePopup.setWidth("150px");
	    simplePopup.setWidget(new HTML(message));
	    simplePopup.center();
	}

}
