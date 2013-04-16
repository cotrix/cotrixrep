package org.cotrix.web.publish.client.view;

import org.cotrix.web.publish.client.resources.CotrixPublishResources;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CodeListDetailViewImpl extends Composite implements
		CodeListDetailView, ContextMenuHandler {

	@UiTemplate("CodeListDetail.ui.xml")
	interface CodeListDetailUiBinder extends
			UiBinder<Widget, CodeListDetailViewImpl> {
	}

	private static CodeListDetailUiBinder uiBinder = GWT
			.create(CodeListDetailUiBinder.class);

	CotrixPublishResources resources = GWT.create(CotrixPublishResources.class);

	private Presenter presenter;
	private boolean isShowingNavLeft = true;
	@UiField Label codelistName;
	@UiField VerticalPanel loadingPanel;
	@UiField HTMLPanel contentPanel;
	@UiField HTMLPanel blankPanel;
	@UiField Style style;
	private int row  = -1;
	private int column  = -1;
	
	interface Style extends CssResource {
		String nav();
		String pager();
		String contextMenu();
		String flexTable();
	}
	private PopupPanel contextMenu;


	private class FlexColumn extends Column<String[],String> {
		int index;
		public FlexColumn(Cell<String> cell,int index) {
			super(cell);
			this.index = index;
		}
		public String getValue(String[] column) {
			return column[index];
		}
	}


	public CodeListDetailViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}


	public void init() {

	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}


	private void showContentPanel() {
		this.loadingPanel.setVisible(false);
		this.contentPanel.setVisible(true);
		this.blankPanel.setVisible(false);
	}

	public void showActivityIndicator() {
		this.contentPanel.setVisible(false);
		this.blankPanel.setVisible(false);
		this.loadingPanel.setVisible(true);
	}

	public void setData(CotrixImportModel model, int id) {
		Metadata metadata = model.getMetadata();

		this.codelistName.setText(metadata.getName());
		showContentPanel();
	}

	public void onContextMenu(ContextMenuEvent event) {
		// stop the browser from opening the context menu
		event.preventDefault();
		event.stopPropagation();
		this.contextMenu.setPopupPosition(event.getNativeEvent().getClientX(),
				event.getNativeEvent().getClientY());
		this.contextMenu.show();
	}



}
