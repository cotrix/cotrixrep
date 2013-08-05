package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.shared.ReportLog;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

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
	
	@UiField(provided = true)
	DataGrid<ReportLog> reportGrid;
	@UiField(provided = true)
	SimplePager reportPager;
	
	@UiField Button importButton;
	@UiField Button manageButton;
	
	protected Presenter presenter;
	protected ReportLogDataProvider dataProvider;

	@Inject
	public DoneStepViewImpl(ReportLogDataProvider dataProvider) {
		this.dataProvider = dataProvider;
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
		if (visible) {
			reportGrid.onResize();
			//reportGrid.setVisibleRange(0, reportPager.getPageSize());
			reportPager.setPage(0);
		}
	}
	
	protected void setupGrid()
	{
		reportGrid = new DataGrid<ReportLog>();
		reportGrid.setWidth("100%");
		reportGrid.setHeight("300px");
		reportGrid.setPageSize(10);

		reportGrid.setAutoHeaderRefreshDisabled(true);

		reportGrid.setEmptyTableWidget(new Label("No data"));

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		reportPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		reportPager.setDisplay(reportGrid);

		Column<ReportLog, String> typeColumn = new Column<ReportLog, String>(new TextCell()) {
			@Override
			public String getValue(ReportLog object) {
				return object.getType().toString();
			}
		};
		typeColumn.setSortable(false);
		/*sortHandler.setComparator(firstNameColumn, new Comparator<ContactInfo>() {
	      @Override
	      public int compare(ContactInfo o1, ContactInfo o2) {
	        return o1.getFirstName().compareTo(o2.getFirstName());
	      }
	    });*/
		reportGrid.addColumn(typeColumn, "Type");
		reportGrid.setColumnWidth(typeColumn, "100px");
		
		Column<ReportLog, String> messageColumn = new Column<ReportLog, String>(new TextCell()) {
			@Override
			public String getValue(ReportLog object) {
				return object.getMessage();
			}
		};
		messageColumn.setSortable(false);
		/*sortHandler.setComparator(firstNameColumn, new Comparator<ContactInfo>() {
			      @Override
			      public int compare(ContactInfo o1, ContactInfo o2) {
			        return o1.getFirstName().compareTo(o2.getFirstName());
			      }
			    });*/
		reportGrid.addColumn(messageColumn, "Message");

		dataProvider.addDataDisplay(reportGrid);
		
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void setMessage(String message) {
		
	}

	public void setStepTitle(String title) {
		this.title.setText(title);
	}
	
	public void setReportPanelVisible(boolean visible)
	{
		//reportPanel.setVisible(visible);
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
