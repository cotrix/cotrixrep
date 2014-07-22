package org.cotrix.web.publish.client.wizard.step.cometmapping;

import org.cotrix.web.publish.shared.DefinitionsMappings;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(CometMappingStepViewImpl.class)
public interface CometMappingStepView {
	
	public interface Presenter {
		public void onReload();
	}
	
	public void setPresenter(Presenter presenter);
	
	public void setCsvName(String name);
	public String getCsvName();
	
	public void setMappingLoading();
	public void setMappings(DefinitionsMappings mapping);
	public void unsetMappingLoading();
	
	public void setCodeTypeError();
	public void cleanStyle();
	
	public DefinitionsMappings getMappings();
	
	Widget asWidget();

	void setVersion(String version);

	String getVersion();

	void setSealed(boolean sealed);

	boolean getSealed();

	void showMetadata(boolean visible);

}
