package org.cotrix.web.importwizard.client.step.sourceselection;

import org.cotrix.web.share.client.widgets.AlertDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SourceSelectionStepViewImpl extends Composite implements SourceSelectionStepView {

	@UiTemplate("SourceSelectionStep.ui.xml")
	interface SourceSelectionStepUiBinder extends UiBinder<Widget, SourceSelectionStepViewImpl> {}
	private static SourceSelectionStepUiBinder uiBinder = GWT.create(SourceSelectionStepUiBinder.class);
	
	private Presenter presenter;
	
	public SourceSelectionStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("cloudPanel")
	public void onCloudClicked(ClickEvent event){
		presenter.onCloudButtonClick();
	}
	
	@UiHandler("myComputerPanel")
	public void onLocalClicked(ClickEvent event){
		presenter.onLocalButtonClick();
	}
	
	public void alert(String message) {
		AlertDialog.INSTANCE.center(message);
	}
}
