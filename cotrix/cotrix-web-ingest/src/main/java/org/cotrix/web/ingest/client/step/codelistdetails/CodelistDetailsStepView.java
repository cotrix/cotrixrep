package org.cotrix.web.ingest.client.step.codelistdetails;

import org.cotrix.web.ingest.shared.AssetDetails;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface CodelistDetailsStepView {
	
	public interface Presenter {
		public void repositoryDetails();
	}
	
	public void setAssetDetails(AssetDetails asset);
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
