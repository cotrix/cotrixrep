package org.cotrix.web.codelistmanager.client.codelists;

import org.cotrix.web.codelistmanager.shared.CodeListGroup.Version;
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
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListsViewImpl extends ResizeComposite implements CodeListsView, KeyPressHandler, KeyDownHandler {

	private static CodeListsViewUiBinder uiBinder = GWT.create(CodeListsViewUiBinder.class);

	@UiTemplate("CodeListsView.ui.xml")
	interface CodeListsViewUiBinder extends UiBinder<Widget, CodeListsViewImpl> {}

	@UiField(provided=true) 
	CellTree codelists;
	
	//@UiField FlowPanel filterPanel;
	//@UiField PromptedTextBox filterTextBox;
	
	@UiFactory
	PromptedTextBox getPromptedTextBox() {
		return new PromptedTextBox("  Filter...", style.promptTextBox(),
				style.filterTextBox());
	}

	protected CodeListDataProvider codeListDataProvider;
	
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
	public CodeListsViewImpl(CodeListDataProvider codeListDataProvider) {
		this.codeListDataProvider = codeListDataProvider;
		setupCellList();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	protected void setupCellList()
	{
	
		final SingleSelectionModel<Version> selectionModel = new SingleSelectionModel<Version>();
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	  Version selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	        	presenter.onCodelistItemSelected(selected.toUICodelist());
	        }
	      }
	    });
	    
		codelists = new CellTree(new CodeListTreeModel(codeListDataProvider, selectionModel), null);
		
		codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
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
		codeListDataProvider.loadData();
		//codelists.setVisibleRangeAndClearData(codelists.getVisibleRange(), true);
	}
	
	protected class CodeListCell extends AbstractCell<UICodelist> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,	UICodelist value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getName());
		}
	}
}
