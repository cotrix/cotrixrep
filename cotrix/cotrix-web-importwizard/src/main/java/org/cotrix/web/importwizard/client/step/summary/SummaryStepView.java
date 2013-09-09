package org.cotrix.web.importwizard.client.step.summary;

import java.util.List;
import java.util.Map;

import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.MappingMode;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface SummaryStepView {
	
	public interface Presenter {
		
	}

	public void setMapping(List<AttributeMapping> mapping);
	
	void setCodelistName(String name);
	
	void setMetadataAttributes(Map<String, String> properties);
	
	public void setFileName(String fileName);
	public void setFileNameVisible(boolean visible);
	
	public MappingMode getMappingMode();
	public void setMappingMode(MappingMode mode);
	public void setMappingModeVisible(boolean visible);

	Widget asWidget();
}
