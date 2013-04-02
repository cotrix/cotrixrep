package org.cotrix.web.importwizard.client.view.form;

import java.util.HashMap;

import org.cotrix.web.importwizard.client.presenter.SummaryFormPresenterImpl;
import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SummaryFormViewImpl extends Composite implements SummaryFormView<SummaryFormViewImpl>{

	@UiTemplate("SummaryForm.ui.xml")
	interface SummaryFormUiBinder extends UiBinder<Widget, SummaryFormViewImpl> {}
	private static SummaryFormUiBinder uiBinder = GWT.create(SummaryFormUiBinder.class);


	@UiField HTMLPanel panel;
	@UiField FlexTable flexTable;
	@UiField Style style;
	@UiField FlowPanel gwtUploadPanel;

	interface Style extends CssResource {
		String headerlabel();
		String valuelabel();
		String flexTable();
		String flexTableHeader();
		String grid();
		String cell();
		String metadata();
		String metadataLabel();

	}

	private String[] headers;
	private Presenter<SummaryFormPresenterImpl> presenter;
	private AlertDialog alertDialog;
	public SummaryFormViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initForm(String[] headers) {
		Grid grid = new Grid(headers.length, 2);

		for (int i = 0; i < headers.length; i++) {
			Label headerLabel = new Label(headers[i]);
			headerLabel.setStyleName(style.headerlabel());

			Label valueLabel = new Label("value"+i+1);
			valueLabel.setStyleName(style.valuelabel());

			grid.getCellFormatter().setStyleName(i, 0, style.cell());
			grid.setWidget(i, 0, headerLabel);
			grid.setWidget(i, 1, valueLabel);
		}
		
	}

	public void setPresenter(SummaryFormPresenterImpl presenter) {
		this.presenter = presenter;
	}

	public void setHeader(String[] headers) {
		this.headers = headers;
		flexTable.setWidget(0, 0, new HTML("Header"));
		flexTable.setWidget(0, 1, new HTML("Type"));
		flexTable.setWidget(0, 2, new HTML("Description"));
		flexTable.setWidget(0, 3, new HTML("Metadata"));
		flexTable.getCellFormatter().setStyleName(0, 0, style.flexTableHeader());
		flexTable.getCellFormatter().setStyleName(0, 1, style.flexTableHeader());
		flexTable.getCellFormatter().setStyleName(0, 2, style.flexTableHeader());
		flexTable.getCellFormatter().setStyleName(0, 3, style.flexTableHeader());

		for (int i = 0; i < headers.length; i++) {
			flexTable.setWidget(i+1, 0, new HTML(headers[i]));
		}
	}

	public void setDescription(HashMap<String, String> description) {
		for (int i = 0; i < headers.length; i++) {
			if(description.containsKey(headers[i])){
				flexTable.setWidget(i+1, 2, new HTML(description.get(headers[i])));
			}
		}
	}

	public void setHeaderType(HashMap<String, HeaderType> headerType) {
		for (int i = 0; i < headers.length; i++) {
			if(headerType.containsKey(headers[i])){
				HeaderType type = headerType.get(headers[i]);
				if(type.getValue() == null){
					flexTable.setWidget(i+1, 1, new HTML("Not Defined"));
				}else{
					if(type.getRelatedValue()!=null){
						String value = type.getValue() + " ( "+type.getRelatedValue()+" )";
						flexTable.setWidget(i+1, 1, new HTML(value));
					}else{
						flexTable.setWidget(i+1, 1, new HTML(type.getValue()));
					}
				}
			}
		}
	}
	

	public void setMetadata(Metadata metadata) {
		flexTable.getFlexCellFormatter().setRowSpan(5, 3, headers.length -4);
		flexTable.setWidget(1, 3, new HTML("<strong>Name : </strong>"+metadata.getName()));
		flexTable.setWidget(2, 3, new HTML("<strong>Owner : </strong>"+metadata.getOwner()));
		flexTable.setWidget(3, 3, new HTML("<strong>Description : </strong>"+metadata.getDescription()));
		flexTable.setWidget(4, 3, new HTML("<strong>Version : </strong>"+metadata.getVersion()));
	}
	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}


}
