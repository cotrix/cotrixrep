package org.cotrix.web.importwizard.client.step.channel;

import java.util.ArrayList;
import org.cotrix.web.importwizard.shared.AssetInfo;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface ChannelStepView {
	
	public interface Presenter {
		public void assetSelected(AssetInfo asset);
	}
	
	void loadAssets(ArrayList<AssetInfo> assets);
	void alert(String message);
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
