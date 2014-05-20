package org.cotrix.web.client.userbar;

import org.cotrix.web.common.client.util.FadeAnimation;
import org.cotrix.web.common.client.util.FadeAnimation.Speed;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class UserBarViewImpl extends Composite implements UserBarView {

	@UiTemplate("UserBar.ui.xml")
	interface UserBarUiBinder extends UiBinder<Widget, UserBarViewImpl> {}
	private static UserBarUiBinder uiBinder = GWT.create(UserBarUiBinder.class);

	interface Style extends CssResource {
		String activeLabelDisabled();
	}
	
	@UiField InlineLabel status;

	@UiField FlowPanel extensionArea;
	@UiField Image loading;
	@UiField FlowPanel userInfo;
	
	@UiField Image user;
	@UiField Image userDisabled;

	@UiField InlineLabel username;
	@UiField InlineHTML logSeparator;
	@UiField InlineLabel login;
	@UiField InlineLabel logout;
	@UiField InlineHTML registerSeparator;
	@UiField InlineLabel register;

	@UiField Style style;

	protected Presenter presenter;
	protected FadeAnimation statusAnimation;

	public UserBarViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		statusAnimation = new FadeAnimation(status.getElement());
	}

	@Override
	public void setStatus(String status)
	{
		this.status.setText(status);
		statusAnimation.fadeOut(Speed.SLOW);
	}
	
	@Override
	public HasWidgets getExtensionArea() {
		return extensionArea;
	}

	@UiHandler("username")
	protected void onUsernameClick(ClickEvent event)
	{
		presenter.onUserClick();
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

	@UiHandler("register")
	protected void onRegisterClick(ClickEvent event)
	{
		presenter.onRegisterClick();
	}
	
	@Override
	public void setUserLoading(boolean loading) {
		this.userInfo.setVisible(!loading);
		this.loading.setVisible(loading);
	}

	@Override
	public void setUserEnabled(boolean enabled) {
		user.setVisible(enabled);
		userDisabled.setVisible(!enabled);
	}

	@Override
	public void setUsername(String username) {
		this.username.setText(username);
	}

	@Override
	public void setUsernameClickEnabled(boolean enabled) {
		this.username.setStyleName(style.activeLabelDisabled(), !enabled);
		if (enabled) sinkUsernameClick();
		else unsinkUsernameClick();
	}

	protected void sinkUsernameClick() {
		int typeInt = Event.getTypeInt(ClickEvent.getType().getName());

		if (typeInt == -1) {
			username.sinkBitlessEvent(ClickEvent.getType().getName());
		} else {
			username.sinkEvents(typeInt);
		}
	}

	protected void unsinkUsernameClick() {
		int typeInt = Event.getTypeInt(ClickEvent.getType().getName());
		username.unsinkEvents(typeInt);
	}

	@Override
	public void setLoginVisible(boolean visible) {
		login.setVisible(visible);
		updateLogSeparatorVisibility();
	}

	@Override
	public void setLogoutVisible(boolean visible) {
		logout.setVisible(visible);
		updateLogSeparatorVisibility();
	}
	
	protected void updateLogSeparatorVisibility() {
		logSeparator.setVisible(login.isVisible()|logout.isVisible());
	}

	@Override
	public void setRegisterVisible(boolean visible) {
		registerSeparator.setVisible(visible);
		register.setVisible(visible);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
}
