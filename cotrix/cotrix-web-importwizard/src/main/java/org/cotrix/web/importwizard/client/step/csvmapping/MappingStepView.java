package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.List;

import org.cotrix.web.importwizard.shared.AttributeMapping;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface MappingStepView {
	
	public interface Presenter {

	}
	
	public void setMapping(List<AttributeMapping> mapping);
	public void setCodeTypeError();
	public void cleanStyle();
	
	public List<AttributeMapping> getMapping();
	
	public void alert(String message);

	
	Widget asWidget();

}
