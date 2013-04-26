package org.cotrix.web.publish.client.view;

import org.cotrix.web.publish.client.CotrixPublishAppGinInjector;
import org.cotrix.web.publish.client.presenter.ChanelPropertyDialogPresenter;
import org.cotrix.web.publish.shared.ChanelPropertyModelController;
import org.cotrix.web.publish.shared.ChanelPropertyModelController.OnChanelPropertyValidated;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
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
	@UiField Label url;
	@UiField Label setPropertyButton;

	private ChanelPropertyModelController model;
	private CotrixPublishAppGinInjector injector = GWT
			.create(CotrixPublishAppGinInjector.class);

	public ChanelPropertyItem(String name, final String desc,ChanelPropertyModelController model) {
		initWidget(uiBinder.createAndBindUi(this));
		this.chanelName.setText(name);

		this.model = model;
		this.model.setOnValidated(this);
		this.setPropertyButton.addClickHandler(this);
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
		if (pass) {
			this.url.setText(model.getChanelProperty().url);
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
