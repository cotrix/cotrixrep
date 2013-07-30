package org.cotrix.web.importwizard.client.step.channel;

import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface SelectionStepView {
	
	public interface Presenter {
		public void assetSelected(AssetInfo asset);
		public void assetDetails(AssetInfo asset);
	}
	
	public void showAssetDetails(AssetDetails asset);
	public void reset();
	void alert(String message);
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
