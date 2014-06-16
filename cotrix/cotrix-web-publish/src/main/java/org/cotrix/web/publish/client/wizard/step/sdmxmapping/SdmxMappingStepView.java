package org.cotrix.web.publish.client.wizard.step.sdmxmapping;

import org.cotrix.web.publish.shared.AttributesMappings;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(SdmxMappingStepViewImpl.class)
public interface SdmxMappingStepView {
	
	public interface Presenter {
		public void onReload();
	}
	
	public void setPresenter(Presenter presenter);
	
	public void setMappingLoading();
	public void unsetMappingLoading();
	
	public void setMappings(AttributesMappings attributes);
	
	public AttributesMappings getMappings();

	
	Widget asWidget();

	void setCodelistName(String name);

	String getCodelistName();

	void setVersion(String version);

	String getVersion();

	void setSealed(boolean sealed);

	boolean getSealed();

	void showMetadata(boolean visible);

}
