/**
 * 
 */
package org.cotrix.web.ingest.client.step.codelistdetails;

import java.util.List;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.Property;
import org.cotrix.web.ingest.shared.AssetDetails;

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
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistDetailsStepViewImpl extends ResizeComposite implements CodelistDetailsStepView {
	
	private static final int ASSET_PROPERTIES_ROW = 4;

	@Inject
	private CodelistDetailsUiBinder uiBinder;
	
	@UiTemplate("CodelistDetailsStep.ui.xml")
	interface CodelistDetailsUiBinder extends UiBinder<Widget, CodelistDetailsStepViewImpl> {
	}

	@UiField Grid assetDetails;
	@UiField Label repository;
	@UiField Label assetName;
	@UiField Label assetVersion;
	@UiField Label assetType;
	@UiField FlexTable assetProperties;
	
	protected Presenter presenter;
	
	@Inject
	private void init() {

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
		assetVersion.setText(asset.getVersion());
		assetType.setText(asset.getType());
		addAssetProperties(asset.getProperties());
		repository.setText(ValueUtils.getLocalPart(asset.getRepositoryName()));
	}

	public void addAssetProperties(List<Property> properties)
	{
		assetProperties.removeAllRows();
		
		boolean empty = properties == null || properties.isEmpty();
		
		assetDetails.getRowFormatter().setVisible(ASSET_PROPERTIES_ROW, !empty);
		
		if (!empty) {
			setupHeaders(assetProperties);
			for (Property property:properties) addRow(assetProperties, property.getName(), property.getValue(), property.getDescription());
		}
	}

	private void setupHeaders(FlexTable table)
	{
		table.getFlexCellFormatter().setColSpan(0, 0, 1);
		table.setWidget(0, 0, getHeaderLabel("Name"));
		table.setWidget(0, 1, getHeaderLabel("Value"));
		table.setWidget(0, 2, getHeaderLabel("Description"));
	}

	private Label getHeaderLabel(String text)
	{
		Label headerLabel = new Label(text);
		headerLabel.setStyleName(CommonResources.INSTANCE.css().propertiesTableHeader());
		return headerLabel;
	}

	private void addRow(FlexTable table, String name, String value, String description)
	{
		int numRows = table.getRowCount();
		table.setWidget(numRows, 0, new Label(check(name)));
		table.setWidget(numRows, 1, new Label(check(value)));
		table.setWidget(numRows, 2, new Label(check(description)));
	}
	
	private String check(String value) {
		return value==null?"":value;
	}
}
