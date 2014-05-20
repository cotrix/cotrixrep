package org.cotrix.web.ingest.client.step.summary;

import java.util.List;
import java.util.Map;

import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.MappingMode;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(SummaryStepViewImpl.class)
public interface SummaryStepView {

	public void setMapping(List<AttributeMapping> mapping);
	
	void setCodelistName(String name);
	
	void setMetadataAttributes(Map<String, String> properties);
	
	public void setFileName(String fileName);
	public void setFileNameVisible(boolean visible);
	
	public MappingMode getMappingMode();
	public void setMappingMode(MappingMode mode);
	public void setMappingModeVisible(boolean visible);

	Widget asWidget();

	void setCodelistVersion(String version);

	void setSealed(boolean sealed);
}
