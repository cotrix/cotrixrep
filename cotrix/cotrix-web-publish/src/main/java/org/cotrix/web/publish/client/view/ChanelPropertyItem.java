package org.cotrix.web.publish.client.view;

import org.cotrix.web.publish.client.CotrixPublishAppGinInjector;
import org.cotrix.web.publish.client.presenter.ChanelPropertyDialogPresenter;
import org.cotrix.web.publish.shared.ChanelPropertyModelController;
import org.cotrix.web.publish.shared.ChanelPropertyModelController.OnChanelPropertyValidated;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ChanelPropertyItem extends Composite implements
		OnChanelPropertyValidated, ClickHandler {

	private static ChanelPropertyItemUiBinder uiBinder = GWT
			.create(ChanelPropertyItemUiBinder.class);

	interface ChanelPropertyItemUiBinder extends
			UiBinder<Widget, ChanelPropertyItem> {
	}

	@UiField Label chanelName;
	@UiField CheckBox checkbox;
	@UiField Image info;
	@UiField Image edit;
	@UiField Label url;

	private ChanelPropertyModelController model;
	private CotrixPublishAppGinInjector injector = GWT.create(CotrixPublishAppGinInjector.class);


	public ChanelPropertyItem(String name, final String desc, ChanelPropertyModelController model) {
		initWidget(uiBinder.createAndBindUi(this));
		this.chanelName.setText(name);
		
		this.model = model;
		this.model.setOnValidated(this);
		this.info.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				InfoDialog info = new InfoDialog(desc);
				info.show();
			}
		});
		this.edit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialog.show();
			}
		});
		this.checkbox.addClickHandler(this);
	}

	public boolean isChecked() {
		return checkbox.getValue();
	}

	public ChanelPropertyModelController getModel() {
		return model;
	}

	public void setModel(ChanelPropertyModelController model) {
		this.model = model;
	}

	public void onValidatedResult(boolean pass) {
		if(pass){
			this.url.setText(model.getChanelProperty().getUrl());
			this.edit.setVisible(true);
		}else{
			this.url.setText("");
			this.edit.setVisible(false);
		}
	}

	private ChanelPropertyDialogPresenter dialog ;
	public void onClick(ClickEvent event) {
		if(checkbox.getValue()){
			dialog = injector.getChanelPropertyDialogPresenter();
			dialog.setModel(model);
			dialog.show();
		}else{
			this.url.setText("");
			this.edit.setVisible(false);
		}
	}

}
