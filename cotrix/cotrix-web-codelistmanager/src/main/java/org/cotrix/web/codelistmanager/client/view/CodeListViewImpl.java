package org.cotrix.web.codelistmanager.client.view;

import org.cotrix.web.codelistmanager.client.resources.CellListResources;
import org.cotrix.web.share.shared.UICodelist;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListViewImpl extends ResizeComposite implements CodeListView, KeyPressHandler, KeyDownHandler{

	private static CodeListViewUiBinder uiBinder = GWT.create(CodeListViewUiBinder.class);

	@UiTemplate("CodeListView.ui.xml")
	interface CodeListViewUiBinder extends UiBinder<Widget, CodeListViewImpl> {}

	@UiField(provided=true) 
	CellList<UICodelist> codelists;
	
	@UiField FlowPanel filterPanel;
	@UiField PromptedTextBox filterTextBox;
	
	@UiFactory
	PromptedTextBox getPromptedTextBox() {
		return new PromptedTextBox("  Filter...", style.promptTextBox(),
				style.filterTextBox());
	}

	private Presenter presenter;
	
	@UiField
	Style style;

	interface Style extends CssResource {
		String filterTextBox();

		String promptTextBox();

		String celllist();

		String cellitem();
	}

	@Inject
	public CodeListViewImpl(CodeListDataProvider codeListDataProvider) {
		setupCellList(codeListDataProvider);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	protected void setupCellList(CodeListDataProvider codeListDataProvider)
	{

		// Create a cell to render each value.
		CodeListCell cell = new CodeListCell();
	    
	    // Create a CellList that uses the cell.
		codelists = new CellList<UICodelist>(cell,CellListResources.INSTANCE);
		codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

	    // Add a selection model to handle user selection.
	    final SingleSelectionModel<UICodelist> selectionModel = new SingleSelectionModel<UICodelist>();
	    codelists.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	 UICodelist selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	        	presenter.onCodelistItemSelected(selected);
	        }
	      }
	    });
	    
	    codeListDataProvider.addDataDisplay(codelists);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/*private List<String> getMatchedItem(String token) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < codelistLabels.size(); i++) {
			if (codelistLabels.get(i).substring(0, token.length()).equalsIgnoreCase(token)) {
				list.add(codelistLabels.get(i));
			}
		}
		return list;
	}*/
	
	public void onKeyPress(KeyPressEvent event) {
		/*String filterText = filterTextBox.getText() + String.valueOf(event.getCharCode());
		List<String> filteredList = getMatchedItem(filterText.trim());
		cellList.setRowData(filteredList);*/
	}

	public void onKeyDown(KeyDownEvent event) {
		/*if(event.getNativeKeyCode() == 8) {
			if(filterTextBox.getText().length() > 0){
				String filterText = filterTextBox.getText().substring(0, filterTextBox.getText().length()-1);
				List<String> filteredList = getMatchedItem(filterText.trim());
				cellList.setRowData(filteredList);
				if(filterText.length() == 0){
					filterTextBox.showPrompt();
					filterTextBox.setCursorPos(0);
				}
			}
		}*/
	}
	
	public void refresh()
	{
		codelists.setVisibleRangeAndClearData(codelists.getVisibleRange(), true);
	}
	
	protected class CodeListCell extends AbstractCell<UICodelist> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,	UICodelist value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getName());
		}
	}
}
