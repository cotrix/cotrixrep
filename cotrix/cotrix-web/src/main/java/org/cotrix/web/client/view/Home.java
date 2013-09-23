package org.cotrix.web.client.view;

import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.CotrixModuleController;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class Home extends Composite implements CotrixModuleController {

	private static HomeUiBinder uiBinder = GWT.create(HomeUiBinder.class);

	interface HomeUiBinder extends UiBinder<Widget, Home> {
	}

	public Home() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public CotrixModule getModule() {
		return CotrixModule.HOME;
	}

	@Override
	public void go(HasWidgets container) {
		container.add(this);		
	}

	@Override
	public void activate() {
	}

	@Override
	public void deactivate() {
	}

}
