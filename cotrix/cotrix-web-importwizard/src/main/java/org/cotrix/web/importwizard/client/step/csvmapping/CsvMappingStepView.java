package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.List;

import org.cotrix.web.importwizard.shared.AttributeMapping;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface CsvMappingStepView {
	
	public interface Presenter {
		public void onReload();
	}
	
	public void setPresenter(Presenter presenter);
	
	public void setCsvName(String name);
	public String getCsvName();
	
	public void setMapping(List<AttributeMapping> mapping);
	public void setCodeTypeError();
	public void cleanStyle();
	
	public List<AttributeMapping> getMappings();
	
	public void alert(String message);

	
	Widget asWidget();

}
