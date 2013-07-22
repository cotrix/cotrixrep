package org.cotrix.web.importwizard.client.step.summary;

import java.util.HashMap;

import org.cotrix.web.share.shared.HeaderType;
import org.cotrix.web.share.shared.Metadata;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface SummaryStepView {
	
	public interface Presenter {
		
	}
	void setHeader(String[] headers);
	void alert(String message);
	void setDescription(HashMap<String, String> description);
	void setHeaderType(HashMap<String,HeaderType> headerType);
	void setMetadata(Metadata metadata);

	Widget asWidget();
}
