package org.cotrix.web.publish.client.wizard.step.done;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class DownloadDialogImpl extends PopupPanel implements DownloadDialog {
	
	private static final Binder binder = GWT.create(Binder.class);
	
	@UiTemplate("DownloadDialog.ui.xml")
	interface Binder extends UiBinder<Widget, DownloadDialogImpl> {}
	
	@UiField Anchor downloadLink;

	public DownloadDialogImpl() {
		setWidget(binder.createAndBindUi(this));
		setAutoHideEnabled(true);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void showCentered(String url) {
		setUrl(url);
		super.center();
		
	}
	
	private void setUrl(String url) {
		downloadLink.setHref(url);
	}
	
}
