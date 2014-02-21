package org.cotrix.web.ingest.client.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.ingest.shared.AttributeMapping;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
public interface SdmxMappingStepView {
	
	public interface Presenter {
		public void onReload();
	}
	
	public void setPresenter(Presenter presenter);
	
	public void setMappingLoading();
	public void unsetMappingLoading();
	
	public void setMappings(List<AttributeMapping> attributes);
	
	public List<AttributeMapping> getMappings();
	
	public void alert(String message);

	
	Widget asWidget();

	void setCodelistName(String name);

	String getCodelistName();

	void setVersion(String version);

	String getVersion();

	void setSealed(boolean sealed);

	boolean getSealed();

}
