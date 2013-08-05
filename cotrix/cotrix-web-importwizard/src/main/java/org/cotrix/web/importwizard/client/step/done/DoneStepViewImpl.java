package org.cotrix.web.importwizard.client.step.done;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DoneStepViewImpl extends Composite implements DoneStepView {

	private static DoneStepViewUiBinder uiBinder = GWT.create(DoneStepViewUiBinder.class);

	@UiTemplate("DoneStepView.ui.xml")
	interface DoneStepViewUiBinder extends UiBinder<Widget, DoneStepViewImpl> {
	}
	
	@UiField Label title;
	@UiField HTMLPanel reportPanel;
	@UiField HTML message;
	@UiField Button importButton;
	@UiField Button manageButton;
	
	protected Presenter presenter;

	public DoneStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void setMessage(String message) {
		this.message.setHTML(message);
	}

	public void setStepTitle(String title) {
		this.title.setText(title);
	}
	
	public void setReportPanelVisible(boolean visible)
	{
		reportPanel.setVisible(visible);
	}

	@UiHandler("importButton")
	protected void onImportButtonClicked(ClickEvent clickEvent)
	{
		presenter.importButtonClicked();
	}
	
	@UiHandler("manageButton")
	protected void onManageButtonClicked(ClickEvent clickEvent)
	{
		presenter.manageButtonClicked();
	}
}
