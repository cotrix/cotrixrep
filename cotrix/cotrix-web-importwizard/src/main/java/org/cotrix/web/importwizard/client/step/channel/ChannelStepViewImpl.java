package org.cotrix.web.importwizard.client.step.channel;

import java.util.ArrayList;

import org.cotrix.web.importwizard.client.util.AlertDialog;
import org.cotrix.web.importwizard.client.step.Style;
import org.cotrix.web.importwizard.shared.AssetInfo;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ChannelStepViewImpl extends Composite implements ChannelStepView {

	protected static final String[] headers = new String[]{"Name", "Type", "Repository"};

	@UiTemplate("ChannelStep.ui.xml")
	interface ChannelStepUiBinder extends UiBinder<Widget, ChannelStepViewImpl> {}

	private static ChannelStepUiBinder uiBinder = GWT.create(ChannelStepUiBinder.class);

	@UiField (provided = true) 
	DataGrid<AssetInfo> dataGrid;

	@UiField(provided = true)
	SimplePager pager;

	@UiField Style style;
	
	protected AssetInfoDataProvider assetInfoDataProvider;

	private AlertDialog alertDialog;

	private Presenter presenter;

	@Inject
	public ChannelStepViewImpl(AssetInfoDataProvider assetInfoDataProvider) {
		this.assetInfoDataProvider = assetInfoDataProvider;
		
		setupGrid();
		initWidget(uiBinder.createAndBindUi(this));
		setHeight("500px");
	}
	

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		//TODO we should use ResizeComposite instead of Composite
		//The problem would be in the DeckLayoutPanel that don't call onresize
		if (visible) dataGrid.onResize();
	}


	protected void setupGrid()
	{
		dataGrid = new DataGrid<AssetInfo>(AssetInfoKeyProvider.INSTANCE);
		dataGrid.setWidth("100%");

		dataGrid.setAutoHeaderRefreshDisabled(true);

		dataGrid.setEmptyTableWidget(new Label("No data"));

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(dataGrid);

		final SingleSelectionModel<AssetInfo> selectionModel = new SingleSelectionModel<AssetInfo>(AssetInfoKeyProvider.INSTANCE);
		selectionModel.addSelectionChangeHandler(new Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				AssetInfo selectedAsset = selectionModel.getSelectedObject();
				presenter.assetSelected(selectedAsset);
			}
		});
		dataGrid.setSelectionModel(selectionModel);

		// Name
		Column<AssetInfo, String> nameColumn = new Column<AssetInfo, String>(new TextCell()) {
			@Override
			public String getValue(AssetInfo object) {
				return object.getName();
			}
		};
		nameColumn.setSortable(false);
		/*sortHandler.setComparator(firstNameColumn, new Comparator<ContactInfo>() {
	      @Override
	      public int compare(ContactInfo o1, ContactInfo o2) {
	        return o1.getFirstName().compareTo(o2.getFirstName());
	      }
	    });*/
		dataGrid.addColumn(nameColumn, "Name");
		

		// type
		Column<AssetInfo, String> typeColumn = new Column<AssetInfo, String>(new TextCell()) {
			@Override
			public String getValue(AssetInfo object) {
				return object.getType();
			}
		};
		typeColumn.setSortable(false);
		/*sortHandler.setComparator(firstNameColumn, new Comparator<ContactInfo>() {
			      @Override
			      public int compare(ContactInfo o1, ContactInfo o2) {
			        return o1.getFirstName().compareTo(o2.getFirstName());
			      }
			    });*/
		dataGrid.addColumn(typeColumn, "Type");
		

		// repository
		Column<AssetInfo, String> repositoryColumn = new Column<AssetInfo, String>(new TextCell()) {
			@Override
			public String getValue(AssetInfo object) {
				return object.getRepositoryName();
			}
		};
		repositoryColumn.setSortable(false);
		/*sortHandler.setComparator(firstNameColumn, new Comparator<ContactInfo>() {
			      @Override
			      public int compare(ContactInfo o1, ContactInfo o2) {
			        return o1.getFirstName().compareTo(o2.getFirstName());
			      }
			    });*/
		dataGrid.addColumn(repositoryColumn, "Repository");
		
		assetInfoDataProvider.addDataDisplay(dataGrid);
		
	}


	@Override
	public void loadAssets(ArrayList<AssetInfo> assets) {

	}


	/*protected void setupHeader() {
		for (int i = 0; i < headers.length; i++) {
			TextBox header = new TextBox();
			header.setText(headers[i]);
			header.setStyleName(style.textbox());
			codelistGrid.setWidget(0, i, header);
			codelistGrid.getCellFormatter().setStyleName(0, i, style.flextTableHeader());
		}
		//setDefaultSelected(false);
	}*/

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/*private void setDefaultSelected(boolean show){
		for (int j = 0; j < columnCount; j++) {
			if(show){
				codelistGrid.getCellFormatter().setStyleName(1, j, style.flextTableHeader());
			}else{
				codelistGrid.getCellFormatter().removeStyleName(1, j, style.flextTableHeader());
			}
		}
	}*/

	/*public void setData(String[] headers, ArrayList<String[]> data) {
		this.columnCount = headers.length;
		for (int i = 0; i < headers.length; i++) {
			codelistGrid.setWidget(1, i, new HTML(headers[i]));
			codelistGrid.getCellFormatter().setStyleName(1, i,style.flextTableHeader());
		}

		int rowCount = (data.size()<8)?data.size():8;
		for (int i = 1; i < rowCount; i++) {
			String[] columns = data.get(i);
			for (int j = 0; j < columns.length; j++) {
				codelistGrid.setWidget(i+1, j, new HTML(columns[j]));
			}
		}
	}

	public ArrayList<String> getHeaders() {
		ArrayList<String> headers = new ArrayList<String>();

		for (int i = 0; i < codelistGrid.getCellCount(1); i++) {
			HTML column = (HTML) codelistGrid.getWidget(1, i);
			headers.add(column.getText());
		}
		return headers;
	}*/

	public void alert(String message) {
		if(alertDialog == null){
			alertDialog = new AlertDialog();
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}



}
