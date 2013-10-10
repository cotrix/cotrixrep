package org.cotrix.web.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserBarViewImpl extends Composite implements UserBarView {

	@UiTemplate("UserBar.ui.xml")
	interface UserBarUiBinder extends UiBinder<Widget, UserBarViewImpl> {}
	private static UserBarUiBinder uiBinder = GWT.create(UserBarUiBinder.class);
	
	@UiField InlineLabel status;
	
	@UiField InlineLabel username;
	@UiField InlineLabel login;
	@UiField InlineLabel logout;
	
	protected Presenter presenter;
	
	public UserBarViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@Override
	public void setStatus(String status)
	{
		this.status.setText(status);
	}

	@UiHandler("login")
	protected void onLoginClick(ClickEvent event)
	{
		presenter.onLoginClick();
	}
	
	@UiHandler("logout")
	protected void onLogoutClick(ClickEvent event)
	{
		presenter.onLogoutClick();
	}
	
	@Override
	public void setUsername(String username) {
		this.username.setText(username);
	}

	@Override
	public void setLoginVisible(boolean visible) {
		login.setVisible(visible);
	}

	@Override
	public void setLogoutVisible(boolean visible) {
		logout.setVisible(visible);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
}
