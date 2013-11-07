package org.cotrix.web.codelistmanager.client.codelists;

import org.cotrix.web.codelistmanager.client.codelists.VersionDialog.VersionDialogListener;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar.ButtonClickedEvent;
import org.cotrix.web.codelistmanager.client.common.ItemToolbar.ButtonClickedHandler;
import org.cotrix.web.codelistmanager.client.resources.CodelistsResources;
import org.cotrix.web.codelistmanager.client.resources.CotrixManagerResources;
import org.cotrix.web.codelistmanager.shared.CodelistGroup;
import org.cotrix.web.codelistmanager.shared.CodelistGroup.Version;
import org.cotrix.web.share.client.util.FilteredCachedDataProvider.Filter;
import org.cotrix.web.share.client.util.SingleSelectionModel;
import org.cotrix.web.share.shared.codelist.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.CellTree.CellTreeMessages;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistsViewImpl extends ResizeComposite implements CodelistsView, VersionDialogListener {

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
	

	protected CodelistsDataProvider codeListDataProvider;
	
	private Presenter presenter;

	protected VersionDialog versionDialog;

	protected SingleSelectionModel<Version> selectionModel;

	@Inject
	public CodelistsViewImpl(CodelistsDataProvider codeListDataProvider) {
		this.codeListDataProvider = codeListDataProvider;
		setupCellList();
		initWidget(uiBinder.createAndBindUi(this));
		updateSearchBoxStyle();
		bind();
	}
	
	protected void bind()
	{
		toolbar.addButtonClickedHandler(new ButtonClickedHandler() {
			
			@Override
			public void onButtonClicked(ButtonClickedEvent event) {
				switch (event.getButton()) {
					case MINUS: {
						Version selected = selectionModel.getSelectedObject();
						if (selected!=null)	presenter.onCodelistRemove(selected.toUICodelist()); 
					} break;
					case PLUS: presenter.onCodelistCreate(selectionModel.getSelectedObject()); break;
				}
			}
		});
		
		filterTextBox.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Log.trace("onKeyUp value: "+filterTextBox.getValue()+" text: "+filterTextBox.getText());
				updateFilter();
				updateSearchBoxStyle();
			}
		});
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
		codelists = new CellTree(new CodelistTreeModel(codeListDataProvider, selectionModel), null, CodelistsResources.INSTANCE, treeMessages);
		
		//codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void showVersionDialog(Version oldVersion)
	{
		if (versionDialog == null) versionDialog = new VersionDialog(this);
		versionDialog.setOldVersion(oldVersion.getId(), oldVersion.getParent().getName(),  oldVersion.getVersion());
		versionDialog.center();
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
		filterTextBox.setStyleName(CotrixManagerResources.INSTANCE.css().searchBackground(), filterTextBox.getValue().isEmpty());
	}
	
	protected class CodeListCell extends AbstractCell<UICodelist> {

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,	UICodelist value, SafeHtmlBuilder sb) {
			sb.appendEscaped(value.getName());
		}
	}

	@Override
	public void onCreate(String id, String newVersion) {
		presenter.onCodelistNewVersion(id, newVersion);
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
