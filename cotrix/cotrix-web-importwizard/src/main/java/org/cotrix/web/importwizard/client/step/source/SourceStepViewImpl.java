package org.cotrix.web.importwizard.client.step.source;

import org.cotrix.web.importwizard.client.util.AlertDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SourceStepViewImpl extends Composite implements SourceStepView {

	@UiTemplate("SourceStep.ui.xml")
	interface SourceStepUiBinder extends UiBinder<Widget, SourceStepViewImpl> {}
	private static SourceStepUiBinder uiBinder = GWT.create(SourceStepUiBinder.class);
	
	@UiField Button cloudButton;
	@UiField Button localButton;
	
	private AlertDialog alertDialog;
	private Presenter presenter;
	
	public SourceStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("cloudButton")
	public void onCloudButtonClicked(ClickEvent event){
		presenter.onCloudButtonClick();
	}
	
	@UiHandler("localButton")
	public void onLocalButtonClicked(ClickEvent event){
		presenter.onLocalButtonClick();
	}
	
	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}
}
