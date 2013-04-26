package org.cotrix.web.publish.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.cotrix.web.publish.client.resources.CellListResources;
import org.cotrix.web.share.shared.Codelist;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class CodeListViewImpl extends Composite implements CodeListView,
		KeyPressHandler ,KeyDownHandler{

	private static CodeListViewUiBinder uiBinder = GWT.create(CodeListViewUiBinder.class);

	@UiTemplate("CodeListView.ui.xml")
	interface CodeListViewUiBinder extends UiBinder<Widget, CodeListViewImpl> {}

	@UiField FlowPanel panel;
	@UiField FlowPanel listPanel;
	@UiField FlowPanel filterPanel;
	@UiField PromptedTextBox filterTextBox;

	@UiFactory
	PromptedTextBox getPromptedTextBox() {
		return new PromptedTextBox("  Filter...", style.promptTextBox(),
				style.filterTextBox());
	}

	private Presenter presenter;
	private CellList<String> cellList;
	private ArrayList<Codelist> codelists;
	private List<String> codelistLabels;
	private HashMap<String, Integer> codelistId ;
	
	@UiField
	Style style;

	interface Style extends CssResource {
		String filterTextBox();
		String promptTextBox();
		String celllist();
		String cellitem();
	}

	public CodeListViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	private List<String> getMatchedItem(String token) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < codelistLabels.size(); i++) {
			if (codelistLabels.get(i).substring(0, token.length()).equalsIgnoreCase(token)) {
				list.add(codelistLabels.get(i));
			}
		}
		return list;
	}
	
	public void onKeyPress(KeyPressEvent event) {
		String filterText = filterTextBox.getText() + String.valueOf(event.getCharCode());
		List<String> filteredList = getMatchedItem(filterText.trim());
		cellList.setRowData(filteredList);
	}

	public void onKeyDown(KeyDownEvent event) {
		if(event.getNativeKeyCode() == 8) {
			if(filterTextBox.getText().length() > 0){
				String filterText = filterTextBox.getText().substring(0, filterTextBox.getText().length()-1);
				List<String> filteredList = getMatchedItem(filterText.trim());
				cellList.setRowData(filteredList);
				if(filterText.length() == 0){
					filterTextBox.showPrompt();
					filterTextBox.setCursorPos(0);
				}
			}
		}
	}

	private List<String> toRowData(ArrayList<Codelist> codelists){
		List<String> list = new ArrayList<String>();
		for (Codelist codelist : codelists) {
			list.add(codelist.getName());
		}
		return list;
	}
	
	private HashMap<String, Integer> toHashMap(ArrayList<Codelist> codelists){
		HashMap<String, Integer> codelistId = new HashMap<String, Integer>();
		for (Codelist codelist : codelists) {
			codelistId.put(codelist.getName(), codelist.getId());
		}
		return codelistId;
	}
	
	public void init(ArrayList<Codelist> codelists) {
		listPanel.clear();
		
		this.codelists  = codelists;
		this.codelistLabels = toRowData(codelists);
		this.codelistId = toHashMap(codelists);
		
		filterTextBox.addKeyPressHandler(this);
		filterTextBox.addKeyDownHandler(this);
		CellListResources.INSTANCE.cellListStyle().ensureInjected();

		// Create a cell to render each value.
	    TextCell textCell = new TextCell();

	    // Create a CellList that uses the cell.
	    cellList = new CellList<String>(textCell,CellListResources.INSTANCE);
	    cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
	    cellList.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	        String selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	        	presenter.onCodelistItemClicked(codelistId.get(selected));
	        }
	      }
	    });

	    // Set the total row count. This isn't strictly necessary, but it affects
	    // paging calculations, so its good habit to keep the row count up to date.
	    cellList.setRowCount(codelists.size(), true);

	    // Push the data into the widget.
	    cellList.setRowData(0, codelistLabels);
		listPanel.add(cellList);
	}


}
