package org.cotrix.web.publish.client.view;

import java.util.ArrayList;

import org.cotrix.web.publish.client.resources.CotrixPublishResources;
import org.cotrix.web.share.shared.CotrixImportModel;
import org.cotrix.web.share.shared.Metadata;
import org.cotrix.web.share.shared.UIChanel;

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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
	@UiField Image nav;
	@UiField HTMLPanel blankPanel;
	@UiField HTMLPanel chanelList;
	@UiField Style style;

	private ArrayList<ChanelPropertyItem> items = new ArrayList<ChanelPropertyItem>();
	
	@UiHandler("nav")
	public void onNavLeftClicked(ClickEvent event) {
		presenter.onNavLeftClicked(this.isShowingNavLeft);
	}

	interface Style extends CssResource {
		String nav();
		String pager();
		String contextMenu();
		String flexTable();
		String next();
		String title();
		String titleWrapper();
		String icon();
	}

	private PopupPanel contextMenu;
	private Button publishButton;

	private class FlexColumn extends Column<String[], String> {
		int index;

		public FlexColumn(Cell<String> cell, int index) {
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

	public void addChanelItem(ChanelPropertyItem item) {
		this.items.add(item);
		this.chanelList.add(item);
	}

	public void addPublishButton() {
		publishButton = new Button("Publish");
		publishButton.setStyleName(style.next());
		publishButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				presenter.onPublishButtonClicked();
//				final ProgressbarDialog dialog = new ProgressbarDialog();
//				dialog.show();
//
//				Timer t = new Timer() {
//					public void run() {
//						dialog.finish();
//					}
//				};
//
//				t.schedule(3000);
			}
		});
		this.chanelList.add(publishButton);
	}
	public void addTitle(String title,final String description){
		Label l = new Label(title);
		l.setStyleName(style.title());

		CotrixPublishResources resources = GWT.create(CotrixPublishResources.class);
		Image info = new Image();
		info.setResource(resources.info());
		info.setStyleName(style.icon());
		info.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				InfoDialog dialog = new InfoDialog(description);
				dialog.show();
			}
		});

		HorizontalPanel panel = new HorizontalPanel();
		panel.setStyleName(style.titleWrapper());
		panel.add(l);
		panel.add(info);
		this.chanelList.add(panel);
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

	public void setData(CotrixImportModel model, String id) {
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
	public void showNavLeft() {
		nav.setStyleName(style.nav());
		nav.setUrl(resources.nav_collapse_left().getSafeUri().asString());
		isShowingNavLeft = true;
	}

	public void showNavRight() {
		nav.setStyleName(style.nav());
		nav.setUrl(resources.nav_collapse_right().getSafeUri().asString());
		isShowingNavLeft = false;
	}

	public ArrayList<String> getUserSelectedChanels() {
		ArrayList<String> chanels = new ArrayList<String>();
		for (ChanelPropertyItem item : items) {
			UIChanel uiChanel = item.getUIChanel();
			if(item.isSelected()){
				chanels.add(uiChanel.getName())  ;
			}
		}
		return chanels;
	}
}
