package org.cotrix.web.codelistmanager.client.view;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CodeListViewImpl extends Composite implements CodeListView {

	private static CodeListViewUiBinder uiBinder = GWT.create(CodeListViewUiBinder.class);

	@UiTemplate("CodeListView.ui.xml")
	interface CodeListViewUiBinder extends UiBinder<Widget, CodeListViewImpl> {}

	@UiField FlowPanel panel;
	@UiField FlowPanel listPanel;
	@UiField FlowPanel filterPanel;
	@UiField PromptedTextBox filterTextBox;
	@UiFactory PromptedTextBox getPromptedTextBox() { 
		return new PromptedTextBox("  Filter...", style.promptTextBox(),style.filterTextBox());
	}

	private Presenter presenter;

	@UiField Style style;
	interface Style extends CssResource {
		String filterTextBox();
		String promptTextBox();
	}
	public CodeListViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter( Presenter presenter) {
		this.presenter  = presenter;
	}

	private static final List<String> DAYS = Arrays.asList("Sunday", "Monday",
			"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

	public void init() {
		// Create a cell that will interact with a value updater.
		TextInputCell inputCell = new TextInputCell();

		// Create a CellList that uses the cell.
		CellList<String> cellList = new CellList<String>(inputCell);

		// Create a value updater that will be called when the value in a cell
		// changes.
		ValueUpdater<String> valueUpdater = new ValueUpdater<String>() {
			public void update(String newValue) {
				Window.alert("You typed: " + newValue);
			}
		};

		// Add the value updater to the cellList.
		cellList.setValueUpdater(valueUpdater);

		// Set the total row count. This isn't strictly necessary, but it affects
		// paging calculations, so its good habit to keep the row count up to date.
		cellList.setRowCount(DAYS.size(), true);

		// Push the data into the widget.
		cellList.setRowData(0, DAYS);
		listPanel.add(cellList);

	}

}
