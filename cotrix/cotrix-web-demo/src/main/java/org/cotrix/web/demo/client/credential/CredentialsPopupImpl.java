package org.cotrix.web.demo.client.credential;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CredentialsPopupImpl extends PopupPanel implements CredentialsPopup {
	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiTemplate("CredentialsPopup.ui.xml")
	interface Binder extends UiBinder<Widget, CredentialsPopupImpl> {}
	
	public CredentialsPopupImpl() {
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void showCentered() {
		super.center();
	}
	
}
