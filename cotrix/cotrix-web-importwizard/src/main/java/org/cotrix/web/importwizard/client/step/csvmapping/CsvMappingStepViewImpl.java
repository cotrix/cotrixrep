package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.List;

import org.cotrix.web.importwizard.client.util.MappingPanel;
import org.cotrix.web.importwizard.client.util.MappingPanel.ReloadButtonHandler;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.share.client.util.AlertDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

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
	
	protected AlertDialog alertDialog;

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
		if(alertDialog == null){
			alertDialog = new AlertDialog(false);
		}
		alertDialog.setMessage(message);
		alertDialog.show();
	}

	@Override
	public void onReloadButtonClicked() {
		presenter.onReload();
	}

}
