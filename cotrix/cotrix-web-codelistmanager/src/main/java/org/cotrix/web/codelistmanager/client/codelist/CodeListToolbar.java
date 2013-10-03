/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist;

import org.cotrix.web.codelistmanager.client.CotrixManagerAppGinInjector;
import org.cotrix.web.share.client.event.FeatureAsyncCallBack;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeListToolbar extends Composite {
	
	@UiTemplate("CodeListToolbar.ui.xml")
	interface CodelistToolbarUiBinder extends UiBinder<Widget, CodeListToolbar> {}
	private static CodelistToolbarUiBinder uiBinder = GWT.create(CodelistToolbarUiBinder.class);
	
	@UiField Button metadata;
	@UiField Button lock;
	@UiField Button unlock;
	@UiField Button seal;
	
	public CodeListToolbar() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	boolean meta = true;
	
	@UiHandler("metadata")
	protected void onMetadataClick(ClickEvent event)
	{
		this.meta = !meta;
		metadata.setText(meta?"Metadata":"Data");
		if (clickListener!=null) clickListener.onClick();
	}
	
	@UiHandler("save")
	protected void onSaveClick(ClickEvent event)
	{
		Log.trace("onSaveClick");
		CotrixManagerAppGinInjector.INSTANCE.getRpcService().saveMessage("Test message", FeatureAsyncCallBack.wrap(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("save message", caught);
			}

			@Override
			public void onSuccess(Void result) {
				Log.trace("Save back");
			}
		}));
	}

	public Button getLockButton()
	{
		return lock;
	}

	public Button getUnlockButton()
	{
		return unlock;
	}

	public Button getSealButton()
	{
		return seal;
	}
	
	protected ClickListener clickListener;
	
	
	
	/**
	 * @param clickListener the clickListener to set
	 */
	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}



	interface ClickListener {
		public void onClick();
	}

}
