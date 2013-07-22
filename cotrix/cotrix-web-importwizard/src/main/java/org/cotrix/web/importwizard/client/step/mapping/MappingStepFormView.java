package org.cotrix.web.importwizard.client.step.mapping;

import java.util.List;

import org.cotrix.web.importwizard.shared.ColumnDefinition;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface MappingStepFormView {
	
	public interface Presenter {

	}
	
	public void setColumns(List<ColumnDefinition> columns);
	public void setCodeTypeError();
	public void cleanStyle();
	
	public List<ColumnDefinition> getColumns();

	
	Widget asWidget();

}
