package org.cotrix.web.common.client.widgets.dialog;

import org.cotrix.web.common.shared.LongTaskProgress;

import com.google.gwt.core.client.Callback;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(ProgressDialogImpl.class)
public interface ProgressDialog {
	
	public interface ProgressCallBack extends Callback<LongTaskProgress, Throwable> {
		public void onCancel();		
	}
	
	public void show(String progressToken);
	public void show(String progressToken, ProgressCallBack callback);
	public void hide();
}