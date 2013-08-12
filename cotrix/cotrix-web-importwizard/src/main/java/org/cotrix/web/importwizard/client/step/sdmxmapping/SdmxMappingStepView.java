package org.cotrix.web.importwizard.client.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.importwizard.shared.AttributeMapping;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface SdmxMappingStepView {
	
	public interface Presenter {

	}
	
	public void setAttributes(List<AttributeMapping> attributes);
	
	public List<AttributeMapping> getMappings();
	
	public void alert(String message);

	
	Widget asWidget();

	void setCodelistName(String name);

	String getCodelistName();

}
