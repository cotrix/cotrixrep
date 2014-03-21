package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.util.FilteredCachedDataProvider.Filter;
import org.cotrix.web.common.client.util.SingleSelectionModel;
import org.cotrix.web.common.client.widgets.ItemToolbar;
import org.cotrix.web.common.client.widgets.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.common.client.widgets.ItemToolbar.ItemButton;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.manage.client.resources.CodelistsResources;
import org.cotrix.web.manage.shared.CodelistGroup;
import org.cotrix.web.manage.shared.CodelistGroup.Version;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.CellTree.CellTreeMessages;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistsViewImpl extends ResizeComposite implements CodelistsView {

	private static CodeListsViewUiBinder uiBinder = GWT.create(CodeListsViewUiBinder.class);

	@UiTemplate("CodelistsView.ui.xml")
	interface CodeListsViewUiBinder extends UiBinder<Widget, CodelistsViewImpl> {}
	
	protected interface TreeMessages extends CellTreeMessages {

	    @DefaultMessage("no matches")
	    String emptyTree();
	}
	
	@UiField TextBox filterTextBox;

	@UiField(provided=true) 
	CellTree codelists;
	
	@UiField ItemToolbar toolbar;

	private CodelistsDataProvider codeListDataProvider;
	
	private Presenter presenter;

	private SingleSelectionModel<Version> selectionModel;
	
	private CodelistsResources resources;

	@Inject
	public CodelistsViewImpl(CodelistsDataProvider codeListDataProvider, CodelistsResources resources) {
		this.codeListDataProvider = codeListDataProvider;
		this.resources = resources;
		setupCellList();
		initWidget(uiBinder.createAndBindUi(this));
		updateSearchBoxStyle();
		toolbar.setVisible(ItemButton.MINUS, false);
	}
	
	@UiHandler("toolbar")
	protected void onButtonClicked(ButtonClickedEvent event) {
		switch (event.getButton()) {
			case MINUS: {
				Version selected = selectionModel.getSelectedObject();
				if (selected!=null)	presenter.onCodelistRemove(selected.toUICodelist()); 
			} break;
			case PLUS: {
				presenter.onCodelistCreate();
			} break;
		}
	}
	
	@UiHandler("filterTextBox")
	protected void onKeyUp(KeyUpEvent event) {
		Log.trace("onKeyUp value: "+filterTextBox.getValue()+" text: "+filterTextBox.getText());
		updateFilter();
		updateSearchBoxStyle();
	}
	
	protected void setupCellList()
	{
		selectionModel = new SingleSelectionModel<Version>();
	    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	      public void onSelectionChange(SelectionChangeEvent event) {
	    	  System.out.println("SELECTED "+event);
	    	  Version selected = selectionModel.getSelectedObject();
	        if (selected != null) {
	        	presenter.onCodelistItemSelected(selected.toUICodelist());
	        }
	      }
	    });
	    
	    TreeMessages treeMessages = GWT.create(TreeMessages.class); 
		codelists = new CellTree(new CodelistTreeModel(codeListDataProvider, selectionModel), null, resources, treeMessages);
		
		//codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void setAddCodelistVisible(boolean visible)
	{
		toolbar.setVisible(ItemButton.PLUS, visible);
	}
	
	@Override
	public void setRemoveCodelistVisible(boolean visible)
	{
		toolbar.setVisible(ItemButton.MINUS, visible);
	}
	
	@SuppressWarnings("unchecked")
	protected void updateFilter()
	{
		String filter = filterTextBox.getValue();
		if (filter.isEmpty()) codeListDataProvider.unapplyFilters();
		else {
			codeListDataProvider.applyFilters(new ByNameFilter(filter));
		}
	}
	
	public void refresh()
	{
		codeListDataProvider.loadData();
	}
	
	public void updateSearchBoxStyle() {
		filterTextBox.setStyleName(CommonResources.INSTANCE.css().searchBackground(), filterTextBox.getValue().isEmpty());
	}
	
	protected class CodeListCell extends AbstractCell<UICodelist> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,	UICodelist value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getName());
		}
	}
	
	protected class ByNameFilter implements Filter<CodelistGroup> {
		
		protected String name;

		/**
		 * @param name
		 */
		public ByNameFilter(String name) {
			this.name = name.toUpperCase();
		}

		@Override
		public boolean accept(CodelistGroup data) {
			return data.getName().toUpperCase().contains(name);
		}
		
	}
}
