package org.cotrix.web.ingest.client.step.csvmapping;

import java.util.List;

import org.cotrix.web.common.client.widgets.AlertDialog;
import org.cotrix.web.ingest.client.util.MappingPanel;
import org.cotrix.web.ingest.client.util.MappingPanel.ReloadButtonHandler;
import org.cotrix.web.ingest.shared.AttributeMapping;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvMappingStepViewImpl extends ResizeComposite implements CsvMappingStepView, ReloadButtonHandler {

	@UiTemplate("CsvMappingStep.ui.xml")
	interface HeaderTypeStepUiBinder extends UiBinder<Widget, CsvMappingStepViewImpl> {}
	private static HeaderTypeStepUiBinder uiBinder = GWT.create(HeaderTypeStepUiBinder.class);
	
	@UiField(provided = true) MappingPanel mappingPanel;
	
	protected Presenter presenter;
	
	@Inject
	AlertDialog alertDialog;

	public CsvMappingStepViewImpl() {
		mappingPanel = new MappingPanel(true, "COLUMNS");
		mappingPanel.setReloadHandler(this);
		
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void setCsvName(String name) {
		mappingPanel.setName(name);
	}

	@Override
	public String getCsvName() {
		return mappingPanel.getName();
	}
	
	@Override
	public void setVersion(String version) {
		mappingPanel.setVersion(version);
	}

	@Override
	public String getVersion() {
		return mappingPanel.getVersion();
	}
	
	@Override
	public void setSealed(boolean sealed)
	{
		mappingPanel.setSealed(sealed);
	}
	
	@Override
	public boolean getSealed()
	{
		return mappingPanel.getSealed();
	}
	
	public void setMappingLoading()
	{
		mappingPanel.setMappingLoading();
	}
	
	public void unsetMappingLoading()
	{
		mappingPanel.unsetMappingLoading();
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setMapping(List<AttributeMapping> mapping)
	{
		mappingPanel.setMapping(mapping);
	}

	public void setCodeTypeError()
	{
		mappingPanel.setCodeTypeError();
	}

	public void cleanStyle()
	{
		mappingPanel.cleanStyle();
	}

	public List<AttributeMapping> getMappings()
	{
		return mappingPanel.getMappings();
	}
	
	public void alert(String message) {
		alertDialog.center(message);
	}

	@Override
	public void onReloadButtonClicked() {
		presenter.onReload();
	}

}
