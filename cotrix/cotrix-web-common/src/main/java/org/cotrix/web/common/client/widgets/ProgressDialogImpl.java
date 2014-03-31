package org.cotrix.web.common.client.widgets;

import org.cotrix.web.common.client.resources.CommonResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ProgressDialogImpl extends DialogBox implements ProgressDialog {
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiTemplate("ProgressDialog.ui.xml")
	interface Binder extends UiBinder<Widget, ProgressDialogImpl> {}

	public ProgressDialogImpl() {

		CommonResources.INSTANCE.css().ensureInjected();
		setGlassStyleName(CommonResources.INSTANCE.css().glassPanel());
		
		setModal(true);
		setGlassEnabled(true);
		setAnimationEnabled(true);
		
		setWidget(binder.createAndBindUi(this));
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void showCentered() {
		super.center();
	}
	
	
}
