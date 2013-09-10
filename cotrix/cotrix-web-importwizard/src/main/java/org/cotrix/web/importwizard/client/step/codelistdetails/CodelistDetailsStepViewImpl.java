/**
 * 
 */
package org.cotrix.web.importwizard.client.step.codelistdetails;

import java.util.List;

import org.cotrix.web.importwizard.client.resources.Resources;
import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.Property;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistDetailsStepViewImpl extends ResizeComposite implements CodelistDetailsStepView {
	
	protected static final int ASSET_PROPERTIES_ROW = 3;
	protected static final int REPOSITORY_PROPERTIES_ROW = 3;

	private static CodelistDetailsUiBinder uiBinder = GWT.create(CodelistDetailsUiBinder.class);
	
	@UiTemplate("CodelistDetailsStep.ui.xml")
	interface CodelistDetailsUiBinder extends UiBinder<Widget, CodelistDetailsStepViewImpl> {
	}

	@UiField Grid assetDetails;
	@UiField Label repository;
	@UiField Label assetName;
	@UiField Label assetType;
	@UiField FlexTable assetProperties;
	
	protected Presenter presenter;
	

	public CodelistDetailsStepViewImpl() {

		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("repository")
	protected void repositoryClicked(ClickEvent event)
	{
		presenter.repositoryDetails();
	}

	public void setAssetDetails(AssetDetails asset)
	{
		assetName.setText(asset.getName());
		assetType.setText(asset.getType());
		addAssetProperties(asset.getProperties());
		repository.setText(asset.getRepositoryName());
	}

	public void addAssetProperties(List<Property> properties)
	{
		assetProperties.removeAllRows();
		assetDetails.getRowFormatter().setVisible(ASSET_PROPERTIES_ROW, !properties.isEmpty());
		
		if (!properties.isEmpty()) {
			setupHeaders(assetProperties);
			for (Property property:properties) addRow(assetProperties, property.getName(), property.getValue(), property.getDescription());
		}
	}

	protected void setupHeaders(FlexTable table)
	{
		table.getFlexCellFormatter().setColSpan(0, 0, 1);
		table.setWidget(0, 0, getHeaderLabel("Name"));
		table.setWidget(0, 1, getHeaderLabel("Value"));
		table.setWidget(0, 2, getHeaderLabel("Description"));
	}

	protected Label getHeaderLabel(String text)
	{
		Label headerLabel = new Label(text);
		headerLabel.setStyleName(Resources.INSTANCE.css().propertiesTableHeader());
		return headerLabel;
	}

	protected void addRow(FlexTable table, String name, String value, String description)
	{
		int numRows = table.getRowCount();
		table.setWidget(numRows, 0, new Label(name));
		table.setWidget(numRows, 1, new Label(value));
		table.setWidget(numRows, 2, new Label(description));
	}
}
