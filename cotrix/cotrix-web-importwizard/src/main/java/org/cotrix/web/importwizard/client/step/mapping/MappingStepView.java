package org.cotrix.web.importwizard.client.step.mapping;

import java.util.List;

import org.cotrix.web.importwizard.shared.AttributeDefinition;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface MappingStepView {
	
	public interface Presenter {

	}
	
	public void setColumns(List<AttributeDefinition> columns);
	public void setCodeTypeError();
	public void cleanStyle();
	
	public List<AttributeDefinition> getColumns();
	
	public void alert(String message);

	
	Widget asWidget();

}
