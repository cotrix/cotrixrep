package org.cotrix.web.ingest.client.step.csvmapping;

import java.util.List;

import org.cotrix.web.ingest.shared.AttributeMapping;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 */
@ImplementedBy(CsvMappingStepViewImpl.class)
public interface CsvMappingStepView {
	
	public interface Presenter {
		public void onReload();
		public void onPreview();
	}
	
	public void setPresenter(Presenter presenter);
	
	public void setCsvName(String name);
	public String getCsvName();
	
	public void setMappingLoading();
	public void setMapping(List<AttributeMapping> mapping);
	public void unsetMappingLoading();
	
	public void setCodeTypeError();
	public void cleanStyle();
	
	public List<AttributeMapping> getMappings();
	
	public void alert(String message);
	
	Widget asWidget();

	void setVersion(String version);

	String getVersion();

	void setSealed(boolean sealed);

	boolean getSealed();

	void setPreviewVisible(boolean visible);

}
