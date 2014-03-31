package org.cotrix.web.ingest.client.step.selection;

import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.ingest.shared.AssetInfo;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(SelectionStepViewImpl.class)
public interface SelectionStepView {
	
	public interface Presenter {
		public void assetSelected(AssetInfo asset);
		public void assetDetails(AssetInfo asset);
		public void repositoryDetails(UIQName repositoryId);
	}

	public void reset();
	void alert(String message);
	
	void setPresenter(Presenter presenter);
	Widget asWidget();
}
