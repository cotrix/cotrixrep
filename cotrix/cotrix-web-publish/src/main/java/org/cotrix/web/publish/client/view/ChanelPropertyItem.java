package org.cotrix.web.publish.client.view;

import java.util.ArrayList;

import org.cotrix.web.publish.client.CotrixPublishAppGinInjector;
import org.cotrix.web.publish.client.presenter.ChanelPropertyDialogPresenter;
import org.cotrix.web.publish.shared.ChanelPropertyModelController;
import org.cotrix.web.publish.shared.ChanelPropertyModelController.OnChanelPropertyValidated;
import org.cotrix.web.share.shared.UIChanel;
import org.cotrix.web.share.shared.UIChanelAssetType;
import org.cotrix.web.share.shared.UIChanelProperty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class ChanelPropertyItem extends Composite implements
		OnChanelPropertyValidated, ClickHandler {

	private static ChanelPropertyItemUiBinder uiBinder = GWT
			.create(ChanelPropertyItemUiBinder.class);

	interface ChanelPropertyItemUiBinder extends
			UiBinder<Widget, ChanelPropertyItem> {
	}

	@UiField Label chanelName;
	@UiField ListBox types;
	@UiField FlexTable propertyTable;
//	@UiField CheckBox checkbox;
//	@UiField Label url;
//	@UiField Label setPropertyButton;

	private ChanelPropertyModelController model;
	private CotrixPublishAppGinInjector injector = GWT
			.create(CotrixPublishAppGinInjector.class);

	public ChanelPropertyItem(UIChanel uiChanel) {
		initWidget(uiBinder.createAndBindUi(this));
		this.chanelName.setText(uiChanel.getName());
		this.propertyTable.setWidget(0, 0, new HTML("Property"));
		this.propertyTable.setWidget(0, 1, new HTML("Description"));
		this.propertyTable.setWidget(0, 2, new HTML("Value"));
//		this.model = model;
//		this.model.setOnValidated(this);
//		this.setPropertyButton.addClickHandler(this);
	}

//	public boolean isChecked() {
//		return checkbox.getValue();
//	}
	public void addProperties(ArrayList<UIChanelProperty> properties){
		int row = 1;
		for (UIChanelProperty property : properties) {
			this.propertyTable.setWidget(row, 0, new HTML(property.getName()));
			this.propertyTable.setWidget(row, 1, new HTML(property.getDescription()));
			this.propertyTable.setWidget(row, 2, new HTML(property.getValue()));
			row++;
		}
	}
	public ChanelPropertyModelController getModel() {
		return model;
	}
	public void setUIPropertyType(ArrayList<UIChanelAssetType> propertyTypes){
		types.addItem("--Select--", "0");
		for (UIChanelAssetType propertyType : propertyTypes) {
			types.addItem(propertyType.getName(), propertyType.getName());
		}
	}
	public void setModel(ChanelPropertyModelController model) {
		this.model = model;
	}

	public void onValidatedResult(boolean pass) {
		if (pass) {
//			this.url.setText(model.getChanelProperty().url);
		} else {
			
		}
	}

	private ChanelPropertyDialogPresenter dialog;

	public void onClick(ClickEvent event) {
		dialog = injector.getChanelPropertyDialogPresenter();
		dialog.setModel(model);
		dialog.show();
	}

}
