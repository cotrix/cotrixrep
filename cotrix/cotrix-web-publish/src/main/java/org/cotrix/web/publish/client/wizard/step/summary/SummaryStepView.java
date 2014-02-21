package org.cotrix.web.publish.client.wizard.step.summary;

import java.util.List;
import java.util.Map;

import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.MappingMode;

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

	
	public MappingMode getMappingMode();
	public void setMappingMode(MappingMode mode);
	public void setMappingModeVisible(boolean visible);

	Widget asWidget();

	void setCodelistVersion(String version);

	void setState(String state);
}
