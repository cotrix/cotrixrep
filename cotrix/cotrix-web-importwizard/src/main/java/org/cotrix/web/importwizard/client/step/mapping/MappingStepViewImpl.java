package org.cotrix.web.importwizard.client.step.mapping;

import java.util.ArrayList;

import org.cotrix.web.share.shared.HeaderType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MappingStepViewImpl extends Composite implements MappingStepFormView<MappingStepViewImpl> {

	@UiTemplate("MappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, MappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);

	@UiField HTMLPanel panel;
	@UiField Style style;
	interface Style extends CssResource {
		String headerlabel();
		String cell();
		String valuelabel();
	}
	private Grid grid;
	private Presenter<MappingStepPresenterImpl> presenter;
	public void setPresenter(MappingStepPresenterImpl presenter) {
		this.presenter = presenter;
	}

	public MappingStepViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	
	public void setData(String[] headers) {
		grid = new Grid(headers.length, 2);

		for (int i = 0; i < headers.length; i++) {
			Label headerLabel = new Label(headers[i]);
			headerLabel.setStyleName(style.headerlabel());

			HeaderTypePanel h = new HeaderTypePanel();

			grid.getCellFormatter().setStyleName(i, 0, style.cell());
			grid.setWidget(i, 0, headerLabel);
			grid.setWidget(i, 1, h);
		}
		panel.clear();
		panel.add(grid);
	}
	
	public ArrayList<HeaderType> getHeaderTypes() {
		ArrayList<HeaderType> headerType = new ArrayList<HeaderType>();
		for (int i = 0; i < grid.getRowCount(); i++) {
			Label label = (Label) grid.getWidget(i, 0);
			HeaderTypePanel typePanel = (HeaderTypePanel) grid.getWidget(i, 1);
			HeaderType type = typePanel.getHeaderType();
			type.setName(label.getText());
//			Window.alert(type.getName()+"---"+type.getValue());
			headerType.add(type);
		}
		return headerType;
	}

	public void setStyleError() {
		for (int i = 0; i < grid.getRowCount(); i++) {
			Label label = (Label) grid.getWidget(i, 0);
			HeaderTypePanel typePanel = (HeaderTypePanel) grid.getWidget(i, 1);
			HeaderType type = typePanel.getHeaderType();
			if(type.getValue()!=null && type.getValue().equals("Code")){
				typePanel.setStyleError();
			}
		}
	}

}
