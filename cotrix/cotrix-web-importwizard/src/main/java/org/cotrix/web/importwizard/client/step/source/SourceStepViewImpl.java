package org.cotrix.web.importwizard.client.step.source;

import java.util.Date;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SourceStepViewImpl extends Composite implements SourceStepView<SourceStepViewImpl> {

	@UiTemplate("SourceStep.ui.xml")
	interface SourceStepUiBinder extends UiBinder<Widget, SourceStepViewImpl> {}
	private static SourceStepUiBinder uiBinder = GWT.create(SourceStepUiBinder.class);
	
	@UiField Button cloudButton;
	@UiField Button localButton;
	
	private AlertDialog alertDialog;
	private Presenter<SourceStepPresenterImpl> presenter;
	public SourceStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

	}
	
	public void setPresenter(SourceStepPresenterImpl presenter) {
		this.presenter = presenter;
	}
	
	public Metadata getMetadata() {
		Metadata metadata = new Metadata();
		/*metadata.setName(name.getText());
		metadata.setOwner(fileowner.getText());
		metadata.setDescription(description.getText());
		metadata.setVersion(version.getItemText(version.getSelectedIndex()));*/
		return metadata;
	}

	public boolean isValidated() {
		boolean isValidated = true;
		/*if(name.getText().length() == 0){
			presenter.alert("Name is required");
			return false;
		}
		if(fileowner.getText().length() == 0){
			presenter.alert("Ower is required");
			return false;
		}*/
		return isValidated;
	}
	
	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}

}
