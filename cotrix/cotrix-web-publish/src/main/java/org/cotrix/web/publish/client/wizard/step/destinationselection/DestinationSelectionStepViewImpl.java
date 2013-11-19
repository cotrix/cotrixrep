package org.cotrix.web.publish.client.wizard.step.destinationselection;

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
public class DestinationSelectionStepViewImpl extends Composite implements DestinationSelectionStepView {

	@UiTemplate("DestinationSelectionStep.ui.xml")
	interface DestinationSelectionStepUiBinder extends UiBinder<Widget, DestinationSelectionStepViewImpl> {}
	private static DestinationSelectionStepUiBinder uiBinder = GWT.create(DestinationSelectionStepUiBinder.class);
	
	private Presenter presenter;
	
	public DestinationSelectionStepViewImpl() {
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
