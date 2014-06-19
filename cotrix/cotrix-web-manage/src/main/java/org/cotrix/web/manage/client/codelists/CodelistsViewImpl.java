package org.cotrix.web.manage.client.codelists;

import org.cotrix.web.common.client.util.SingleSelectionModel;
import org.cotrix.web.common.client.widgets.SearchBox;
import org.cotrix.web.manage.client.codelists.CodelistTreeModel.Grouping;
import org.cotrix.web.manage.client.codelists.CodelistsToolbar.ButtonClickedEvent;
import org.cotrix.web.manage.client.codelists.CodelistsToolbar.ToolBarButton;
import org.cotrix.web.manage.client.resources.CodelistsResources;
import org.cotrix.web.manage.shared.UICodelistInfo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.CellTree.CellTreeMessages;
import com.google.gwt.user.cellview.client.CustomCellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.UIObject;
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

	@UiField SearchBox filterTextBox;
	
	@UiField PushButton menuButton;

	@UiField(provided=true) 
	CustomCellTree codelists;

	@UiField CodelistsToolbar toolbar;

	private CodelistsDataProvider codeListDataProvider;

	private Presenter presenter;

	private CodelistTreeModel codelistTreeModel;
	private SingleSelectionModel<UICodelistInfo> selectionModel;

	private CodelistsResources resources;

	@Inject
	public CodelistsViewImpl(final CodelistsDataProvider codeListDataProvider, CodelistsResources resources) {
		this.codeListDataProvider = codeListDataProvider;
		this.resources = resources;
		setupCellList();
		initWidget(uiBinder.createAndBindUi(this));

		/*codeListDataProvider.addDataUpdatedHandler(new DataUpdatedHandler() {

			@Override
			public void onDataUpdated(DataUpdatedEvent event) {
				Item selected = selectionModel.getSelectedObject();
				if (selected!=null && !codeListDataProvider.containsVersion(selected)) {
					selectionModel.clear();
				}
			}
		});*/
	}

	@UiHandler("toolbar")
	void onButtonClicked(ButtonClickedEvent event) {
		switch (event.getButton()) {
			case MINUS: {
				UICodelistInfo selected = selectionModel.getSelectedObject();
				if (selected!=null)	presenter.onCodelistRemove(selected); 
			} break;
			case PLUS: {
				presenter.onCodelistCreate();
			} break;
			case VERSION: {
				UICodelistInfo selected = selectionModel.getSelectedObject();
				if (selected!=null)	presenter.onCodelistNewVersion(selected);
			}
		}
	}

	@UiHandler("filterTextBox")
	protected void onValueChange(ValueChangeEvent<String> event) {
		presenter.onFilterQueryChange(event.getValue());
	}

	@UiHandler("menuButton")
	void onClick(ClickEvent event) {
		presenter.onShowMenu();
	}

	protected void setupCellList()
	{
		selectionModel = new SingleSelectionModel<UICodelistInfo>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				fireSelection();
			}
		});

		TreeMessages treeMessages = GWT.create(TreeMessages.class);
		
		codelistTreeModel = new CodelistTreeModel(codeListDataProvider, selectionModel);
		codelists = new CustomCellTree(codelistTreeModel, null, resources, treeMessages);

		//codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		codelists.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
	}

	private void fireSelection() {
		UICodelistInfo selected = selectionModel.getSelectedObject();
		presenter.onCodelistItemSelected((selected != null)?selected:null);
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setAddCodelistVisible(boolean visible)
	{
		toolbar.setVisible(ToolBarButton.PLUS, visible);
	}

	@Override
	public void setVersionCodelistVisible(boolean visible)
	{
		toolbar.setVisible(ToolBarButton.VERSION, visible);
	}

	@Override
	public void setRemoveCodelistVisible(boolean visible)
	{
		toolbar.setVisible(ToolBarButton.MINUS, visible);
	}

	public void reloadData()
	{
		codeListDataProvider.loadData();
	}

	@Override
	public UIObject getMenuTarget() {
		return menuButton;
	}

	@Override
	public void groupBy(Grouping grouping) {
		codelistTreeModel.setGrouping(grouping);
	}
	
	@Override
	public void refreshData() {
		Log.trace("refreshData");
		codeListDataProvider.applyFilters();
	}
}
