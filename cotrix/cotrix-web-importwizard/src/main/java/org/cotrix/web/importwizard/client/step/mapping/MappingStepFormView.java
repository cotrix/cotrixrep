package org.cotrix.web.importwizard.client.step.mapping;

import java.util.ArrayList;

import org.cotrix.web.share.shared.HeaderType;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface MappingStepFormView {
	
	public interface Presenter {

	}
	
	ArrayList<HeaderType> getHeaderTypes();
	void setData(String[] headers);
	void setPresenter(MappingStepPresenterImpl presenter);
	void setStyleError();
	Widget asWidget();

}
